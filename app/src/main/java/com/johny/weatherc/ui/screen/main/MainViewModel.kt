package com.johny.weatherc.ui.screen.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johny.weatherc.data.Resp
import com.johny.weatherc.data.WeatherAction
import com.johny.weatherc.model.WeatherItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherAction: WeatherAction,
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

    private val _showTable = MutableStateFlow(false)
    private val _loading = MutableStateFlow(true)
    private val _uiEvent = MutableStateFlow<MainUiEvent?>(null)
    private val _tip = MutableStateFlow("")
    private val _data = MutableStateFlow<List<WeatherItem>>(emptyList())
    private var fetchJob: Job? = null
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    init {
        viewModelScope.launch {
            combine(_showTable, _loading, _uiEvent, _tip, _data) { showTable, loading, uiEvent, tip, data ->
                State(showTable, loading, uiEvent, tip, data)
            }.collect {
                _state.value = it
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            _loading.value = false
            _uiEvent.value = MainUiEvent.ShowTip("GPS or Network disabled!", false)
            return
        }
        when {
            isNetworkEnabled -> locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10f, locationListener)
            isGpsEnabled -> locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
        }
    }

    fun fetchWeather() {
        fetchJob?.cancel()
        _loading.value = true
        fetchJob = viewModelScope.launch {
            weatherAction.fetchWeather(latitude, longitude).let { resp ->
                if (resp is Resp.Ok) {
                    val tempUnit = resp.data.hourly_units.temperature_2m
                    if (resp.data.hourly.time.size == resp.data.hourly.temperature_2m.size) {
                        _data.value = resp.data.hourly.time.mapIndexed { index, time ->
                            WeatherItem(time, resp.data.hourly.temperature_2m[index], tempUnit)
                        }
                    }
                    _loading.value = false
                    _uiEvent.value = MainUiEvent.ShowTip("Fetch Success", true)
                } else if (resp is Resp.Error) {
                    _uiEvent.value = MainUiEvent.ShowTip("Fetch failed ${resp.e.message}", true)
                }
            }
        }
    }

    fun toggleViewType() {
        _showTable.value = _showTable.value.not()
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