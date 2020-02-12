package com.prasanjit.watstheweather.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prasanjit.watstheweather.service.model.Weather

import java.io.Serializable
import java.lang.reflect.Type

/**
 * Created by Prasanjit on 2019-06-29.
 */
class WeatherDataConverter : Serializable {

    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>?): String? {
        if (weatherList == null) {
            return null
        }

        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.toJson(weatherList, type)
    }

    @TypeConverter
    fun toWeatherList(weatherList: String?): List<Weather>?{
        if(weatherList ==  null){
            return emptyList()
        }

        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(weatherList, type)
    }


}
