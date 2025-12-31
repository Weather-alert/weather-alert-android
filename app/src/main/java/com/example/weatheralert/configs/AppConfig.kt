package com.example.weatheralert.configs

import android.annotation.SuppressLint
import android.provider.Settings

object AppConfig {
    val userServiceBaseUrl = "https://leeanna-genotypical-noncausally.ngrok-free.dev"
    @SuppressLint("HardwareIds")
    val androidId = Settings.Secure.getString(
        AppContextHolder.context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

}