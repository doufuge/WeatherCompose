package com.johny.weatherc.domain.usecase

import com.johny.weatherc.data.Result
import com.johny.weatherc.domain.model.WeatherItem
import com.johny.weatherc.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        hourly: String? = "temperature_2m",
    ): Flow<Result<List<WeatherItem>>> = repository.fetcherWeather(latitude, longitude, hourly)
}