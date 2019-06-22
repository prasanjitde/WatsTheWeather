package com.prasanjit.watstheweather.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class LocationListenerViewModel(application: Application) : AndroidViewModel(application) {

    var currentLocation : MutableLiveData<Location>? = MutableLiveData()

    fun updateLocation(location: Location){
        currentLocation?.value = location
    }

}