package com.prasanjit.watstheweather.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.service.repository.WeatherApiService.weatherInterface
import com.prasanjit.watstheweather.utilities.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Prasanjit on 2019-05-18.
 * This class will be used to fetch weather data from web
 */
class WeatherRepository {

    companion object {
        private val TAG: String = WeatherRepository::class.java.simpleName

        fun getWeatherByCity(city: String): LiveData<WeatherInfo> {
            val data = MutableLiveData<WeatherInfo>()

            val call = weatherInterface.getWeatherByCity(city, Constants.APPID)

            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    Log.d(TAG, "URL " + call.request().url().toString())

                    if (response.isSuccessful) {
                        Log.d(TAG, "Response " + response.message())

                        data.value = response.body()
                    }
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    data.value = null
                    Log.d(TAG, "Error " + t.localizedMessage)
                }
            })

            return data
        }

        /**
         * get weather using co ordinates
         */
        fun getWeatherByCoordinates(lat: Double, lon: Double): LiveData<WeatherInfo> {
            val data = MutableLiveData<WeatherInfo>()

            val call = weatherInterface.getWeatherByCoordinates(lat, lon, Constants.APPID)

            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    Log.d(TAG, "URL " + call.request().url().toString())

                    if (response.isSuccessful) {
                        Log.d(TAG, "Response " + response.message())

                        data.value = response.body()
                    }
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    data.value = null
                    Log.d(TAG, "Error " + t.localizedMessage)
                }
            })

            return data
        }
    }
}