package com.prasanjit.watstheweather.di

import com.prasanjit.watstheweather.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Prasanjit on 2019-10-30.
 */
@Module
class AppModule(application: BaseApplication)  {

    private var application: BaseApplication? = null

    // kotlin constructor could not have code
    // use init for initialization
    init {
        this.application = application
    }

    @Singleton
    @Provides
    fun provideAppContext(): BaseApplication?{
        return application
    }
}