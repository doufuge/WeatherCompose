package com.johny.weatherc.domain.model

data class WeatherItem(
    val hour: String,
    val temp: Float,
    val tempUnit: String = "°C"
)