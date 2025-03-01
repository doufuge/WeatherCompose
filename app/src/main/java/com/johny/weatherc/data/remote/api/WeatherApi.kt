package com.johny.weatherc.data.remote.api

import com.johny.weatherc.data.remote.enitty.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/forecast")
    suspend fun fetchWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String? = "temperature_2m",
    ): WeatherResponse

}