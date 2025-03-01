package com.johny.weatherc.data.remote.enitty

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("generationtime_ms")
    val generationTimeMs: Double,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Int,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val hourly: Hourly,
) : Serializable {

    data class HourlyUnits(
        val time: String,
        @SerializedName("temperature_2m")
        val temperature2m: String,
    )

    data class Hourly(
        val time: List<String>,
        @SerializedName("temperature_2m")
        val temperature2m: List<Float>,
    )

}
