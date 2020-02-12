package com.prasanjit.watstheweather.service.repository

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prasanjit.watstheweather.service.dao.WeatherInfoDao
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.service.repository.WeatherApiService.weatherInterface
import com.prasanjit.watstheweather.utilities.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * Created by Prasanjit on 2019-05-18.
 * This class will be used to fetch weather data from web or Room database
 */
class WeatherRepository(private val weatherInfoDao: WeatherInfoDao, private val application: Application){

    suspend fun insert(weatherInfo: WeatherInfo) {
        val rows = weatherInfoDao.insert(weatherInfo)
        Log.d(TAG, "rows inserted are $rows")
    }

    suspend fun queryForWeather(city: String): WeatherInfo{
        return weatherInfoDao.getWeatherForCity(city)
    }

    companion object {
        private val TAG: String = WeatherRepository::class.java.simpleName
    }

    private fun getWeatherFromDB(city: String): LiveData<WeatherInfo> {
        return weatherInfoDao.getWeatherInfo(city)
    }

    fun getWeather(city: String): LiveData<WeatherInfo>? {

        Log.d(TAG, "City $city")

        // live data will provide value only through observers
        // below statement is always null
        // val weatherModel: WeatherInfo? = weatherDataFromDB?.value
        // Log.d(TAG, " Weather from DB id: ${weatherModel?.id} name: ${weatherModel?.name}")

        return getWeatherByCity(city)
    }

    /*
        Get weather data using city using retrofit
     */
    fun getWeatherByCity(city: String): MutableLiveData<WeatherInfo> {
        val data = MutableLiveData<WeatherInfo>()

        val call = weatherInterface.getWeatherByCity(city, Constants.APPID)

        call.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                Log.d(TAG, "URL " + call.request().url().toString())

                if (response.isSuccessful) {
                    Log.d(TAG, "Response " + response.message())
                    // weatherData.value = response.body()
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                data.value = null
                // weatherData.value = null
                Log.d(TAG, "Error " + t.localizedMessage)
            }
        })

        return data
    }

    /**
     * get weather using co ordinates using retrofit
     */
    fun getWeatherByCoordinates(lat: Double, lon: Double): LiveData<WeatherInfo> {
        val data = MutableLiveData<WeatherInfo>()

        val call = weatherInterface.getWeatherByCoordinates(lat, lon, Constants.APPID)

        call.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                Log.d(TAG, "URL " + call.request().url().toString())

                if (response.isSuccessful) {
                    Log.d(TAG, "Response " + response.message())
                    // weatherData.value = response.body()
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                // weatherData.value = null
                data.value = null
                Log.d(TAG, "Error " + t.localizedMessage)
            }
        })

        return data
    }
}