package com.prasanjit.watstheweather.service.model

import androidx.room.Entity

/**
 * Created by Prasanjit on 2019-06-22.
 */
/*@Entity(
    tableName = "main",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = WeatherInfo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)*/
@Entity
data class Main(
    val temp: Double = 0.0,
    val humidity: Int = 0,
    val id: Int
)