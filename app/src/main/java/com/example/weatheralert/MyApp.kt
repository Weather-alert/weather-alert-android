package com.example.weatheralert

import android.app.Application
import com.example.weatheralert.Log.MyDebugTree
import com.example.weatheralert.configs.AppContextHolder
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextHolder.context = applicationContext
        Timber.plant(MyDebugTree(this))
    }

}
