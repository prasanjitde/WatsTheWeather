package com.prasanjit.watstheweather.service.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Prasanjit on 2019-06-10.
 */
object WeatherApiService {

    /**
     * object is a static class
     * everything inside it is by default static
     * object declaration is initialized lazily when accessed for first time
     * so no need to write lazy
     */

    // still keeping this as lazy but is not required
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val weatherInterface: WeatherInterface = retrofit.create(WeatherInterface::class.java)
}