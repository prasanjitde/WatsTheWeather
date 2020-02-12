package com.prasanjit.watstheweather.di

import com.prasanjit.watstheweather.ui.WeatherFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Prasanjit on 2019-10-30.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(weatherFragment: WeatherFragment)
}