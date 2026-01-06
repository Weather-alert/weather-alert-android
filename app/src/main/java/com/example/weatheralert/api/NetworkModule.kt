package com.example.weatheralert.api

import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.configs.AppContextHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(AppConfig.userServiceBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userService: UserService = UserService(AppContextHolder.context,retrofit.create(UserServiceClient::class.java))
}