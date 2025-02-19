package com.johny.weatherc.model

data class WeatherItem(
    val hour: String,
    val temp: Float,
    val tempUnit: String = "°C"
)