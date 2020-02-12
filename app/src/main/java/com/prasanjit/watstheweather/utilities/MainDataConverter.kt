package com.prasanjit.watstheweather.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prasanjit.watstheweather.service.model.Main
import com.prasanjit.watstheweather.service.model.Weather
import java.io.Serializable

/**
 * Created by Prasanjit on 2019-06-29.
 */
class MainDataConverter: Serializable {

    @TypeConverter
    fun fromMain(main: Main?): String? {
        if (main == null) {
            return null
        }

        val gson = Gson()
        val type = object : TypeToken<Main>() {}.type
        return gson.toJson(main, type)
    }

    @TypeConverter
    fun toMain(main: String?): Main?{
        if(main ==  null){
            return null
        }

        val gson = Gson()
        val type = object : TypeToken<Main>() {}.type
        return gson.fromJson(main, type)
    }

}