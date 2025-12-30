package com.example.weatheralert

import android.app.Application
import android.content.Context
import com.example.weatheralert.Log.MyDebugTree
import com.example.weatheralert.repository.AppContextHolder
import timber.log.Timber

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextHolder.context = applicationContext
        Timber.plant(MyDebugTree(this))
    }

}
