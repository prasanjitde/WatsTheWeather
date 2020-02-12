package com.prasanjit.watstheweather.service.repository

import com.prasanjit.watstheweather.service.model.WeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather?")
    fun getWeatherByCity(
        @Query("q") city: String,
        @Query("APPID") appId: String
    ): Call<WeatherInfo>

    @GET("weather?")
    fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("APPID") appId: String
    ): Call<WeatherInfo>

}

