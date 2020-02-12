package com.prasanjit.watstheweather

import android.app.Application
import com.prasanjit.watstheweather.di.AppComponent
import com.prasanjit.watstheweather.di.AppModule
import com.prasanjit.watstheweather.di.DaggerAppComponent

/**
 * Created by Prasanjit on 2019-10-30.
 */
class BaseApplication: Application() {

    var appComponent:AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun getComponent(): AppComponent? {
        return appComponent
    }
}