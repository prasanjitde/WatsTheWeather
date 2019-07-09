package com.prasanjit.watstheweather.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Prasanjit on 2019-06-22.
 */
object Utilities {
    val TAG = Utilities::class.java.simpleName

    fun convertTempToDegrees(temp: Double?) : Int?{
        return temp?.minus(273.15)?.toInt()
    }

    fun getCurrentTime() : Int{
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when(hour){
            in 5..15 -> 0
            in 16..21 -> 1
            in 22..24 -> 2
            else -> 2
        }
    }
}