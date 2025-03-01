package com.johny.weatherc.presentation.ui.screen.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johny.weatherc.data.Result
import com.johny.weatherc.domain.model.WeatherItem
import com.johny.weatherc.domain.usecase.FetchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        const val DEFAULT_LATITUDE = 52.52
        const val DEFAULT_LONGITUDE = 13.41
    }

    private var latitude = DEFAULT_LATITUDE
    private var longitude = DEFAULT_LONGITUDE

    val locationManager: LocationManager by lazy {
        context.getSystemService(LocationManager::class.java)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
            fetchWeather()
            locationManager.removeUpdates(this)
        }
    }

    private var fetchJob: Job? = null
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    @SuppressLint("MissingPermission")
    fun getLocation() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            _state.update {it.copy(
                loading = false,
                uiEvent = MainUiEvent.ShowTip("GPS or Network disabled!", false)
            )}
            return
        }
        when {
            isNetworkEnabled -> locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10f, locationListener)
            isGpsEnabled -> locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
        }
    }

    fun fetchWeather() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            fetchWeatherUseCase(latitude, longitude).collect { resp ->
                when (resp) {
                    is Result.Loading -> {
                        _state.update { it.copy( loading = false)}
                    }
                    is Result.Ok -> {
                        _state.update { it.copy(
                            loading = false,
                            data = resp.data,
                            uiEvent = MainUiEvent.ShowTip("Fetch weather success!", true))}
                    }
                    is Result.Error -> {
                        _state.update { it.copy(
                            loading = false,
                            uiEvent = MainUiEvent.ShowTip("Fetch weather data error!", false))}
                    }
                }
            }
        }
    }

    fun toggleViewType() {
        _state.update { it.copy(showTable = it.showTable.not()) }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}

data class State(
    val showTable: Boolean = false,
    val loading: Boolean = true,
    val uiEvent: MainUiEvent? = null,
    val tip: String = "",
    val data: List<WeatherItem> = emptyList()
)