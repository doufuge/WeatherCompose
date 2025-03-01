package com.johny.weatherc.data.repository

import com.johny.weatherc.data.Result
import com.johny.weatherc.data.remote.enitty.WeatherResponse
import com.johny.weatherc.data.remote.api.WeatherApi
import com.johny.weatherc.domain.model.WeatherItem
import com.johny.weatherc.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override fun fetcherWeather(
        latitude: Double,
        longitude: Double,
        hourly: String?
    ) : Flow<Result<List<WeatherItem>>> {
        return flow {
            emit(Result.Loading)
            try {
                val weatherList = weatherApi.fetchWeather(latitude, longitude, hourly).toWeatherList()
                emit(Result.Ok(weatherList))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.onEmpty { emit(Result.Ok(emptyList())) }
            .flowOn(Dispatchers.IO)
    }

}

fun WeatherResponse.toWeatherList() = run {
    if (hourly.time.size == hourly.temperature2m.size) {
        val tempUnit = hourlyUnits.temperature2m
        hourly.time.mapIndexed { index, time ->
            WeatherItem(time, hourly.temperature2m[index], tempUnit)
        }
    } else {
        emptyList()
    }
}