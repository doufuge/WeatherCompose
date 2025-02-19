package com.johny.weatherc.data

import com.johny.weatherc.data.api.WeatherApi
import com.johny.weatherc.model.WeatherResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class WeatherAction @Inject constructor(
    private val weatherApi: WeatherApi
) {

    suspend fun fetchWeather(
        latitude: Double,
        longitude: Double,
        hourly: String? = "temperature_2m",
     ): Resp<WeatherResponse> {
        return try {
            Resp.Ok(weatherApi.fetchWeather(latitude, longitude, hourly))
        } catch (e: Exception) {
            Resp.Error(e)
        }
    }

}