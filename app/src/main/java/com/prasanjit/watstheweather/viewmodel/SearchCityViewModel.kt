package com.prasanjit.watstheweather.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prasanjit.watstheweather.BaseApplication
import com.prasanjit.watstheweather.service.model.WeatherInfo
import com.prasanjit.watstheweather.service.repository.SearchCityRepository

/**
 * Created by Prasanjit on 2019-11-22.
 */
class SearchCityViewModel(application: BaseApplication): AndroidViewModel(application) {

    private val searchCityRepository: SearchCityRepository

    init {
        searchCityRepository = SearchCityRepository()
    }

    var weatherData: LiveData<WeatherInfo>? = MutableLiveData()
}