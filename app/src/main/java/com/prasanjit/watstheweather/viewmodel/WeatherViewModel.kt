package com.prasanjit.watstheweather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.service.repository.WeatherRepository

/**
 * Created by Prasanjit on 2019-05-29.
 */
class WeatherViewModel(application: Application): AndroidViewModel(application){
    var weatherData : LiveData<WeatherInfo>? = MutableLiveData()

    fun updateWeatherByCity(city: String) {
        weatherData = WeatherRepository.getWeatherByCity(city)
    }

    fun updateWeatherByCoordinates(lat: Double, lon: Double){
        weatherData = WeatherRepository.getWeatherByCoordinates(lat, lon);
    }
}