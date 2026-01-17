package com.example.weatheralert.configs

import android.annotation.SuppressLint
import android.provider.Settings

object AppConfig {
    val userServiceBaseUrl = "https://4.232.242.151.sslip.io/user/api/v1"
    @SuppressLint("HardwareIds")
    val androidId = Settings.Secure.getString(
        AppContextHolder.context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

}