package com.prasanjit.watstheweather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prasanjit.watstheweather.service.database.WeatherRoomDatabase
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.service.repository.WeatherRepository
import kotlinx.coroutines.launch

/**
 * Created by Prasanjit on 2019-05-29.
 * Weather viewmodel to hold the weather livedata
 * It uses the viewModelScope coroutine for inserting data in Room Database in a separate thread
 */
class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository: WeatherRepository

    init {
        val weatherInfoDao = WeatherRoomDatabase.getDatabase(application, viewModelScope).weatherInfoDao()
        weatherRepository = WeatherRepository(weatherInfoDao, application)
    }

    var weatherData: LiveData<WeatherInfo>? = MutableLiveData()

    fun updateWeather(city: String) {
        weatherData = weatherRepository.getWeather(city)
    }

    fun updateWeatherByCoordinates(lat: Double, lon: Double) {
        weatherData = weatherRepository.getWeatherByCoordinates(lat, lon);
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(weatherInfo: WeatherInfo) = viewModelScope.launch {
        weatherRepository.insert(weatherInfo)
    }
}