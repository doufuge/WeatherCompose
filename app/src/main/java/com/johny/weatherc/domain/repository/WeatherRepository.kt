package com.johny.weatherc.domain.repository

import com.johny.weatherc.domain.model.WeatherItem
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun fetcherWeather(
        latitude: Double,
        longitude: Double,
        hourly: String? = "temperature_2m",
    ): Flow<List<WeatherItem>>

}