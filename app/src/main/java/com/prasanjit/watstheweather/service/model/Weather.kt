package com.prasanjit.watstheweather.service.model

import androidx.room.Entity


/*@Entity(
    tableName = "weather",
    foreignKeys = arrayOf(ForeignKey(
        entity = WeatherInfo::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = CASCADE)
    )
)*/
@Entity
data class Weather(
    val description: String = "",
    val icon: String = "",
    val id: Int
)