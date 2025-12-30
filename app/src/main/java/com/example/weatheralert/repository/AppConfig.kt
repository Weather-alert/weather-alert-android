package com.example.weatheralert.repository

import android.annotation.SuppressLint
import android.provider.Settings
import com.example.weatheralert.repository.AppContextHolder

object AppConfig {
    val userServiceBaseUrl = "http://10.229.47.67:8080/"
    @SuppressLint("HardwareIds")
    val androidId = Settings.Secure.getString(
        AppContextHolder.context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

}