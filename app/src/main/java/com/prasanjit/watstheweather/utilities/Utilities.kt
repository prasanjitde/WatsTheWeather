package com.prasanjit.watstheweather.utilities

/**
 * Created by Prasanjit on 2019-06-22.
 */
object Utilities {
    fun convertTempToDegrees(temp: Double?) : Int?{
        return temp?.minus(273.15)?.toInt()
    }
}