package com.example.weatheralert.api

import android.content.Context
import com.example.weatheralert.api.dataClass.User
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.datastore.AppStateRepository
import kotlinx.coroutines.flow.first
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber

class UserService(
    private val context: Context,
    private val client: UserServiceClient,
) {
    private val userId = AppConfig.androidId

    private val appStateRepository = AppStateRepository(context)

    suspend fun updateUser(req: UserUpdateRequest): Boolean?{
        if(!appStateRepository.appState.first().isRegistered) return null

        return try {
            val r = client.updateUser(userId, req)

            if (r.isSuccessful()) {
                Timber.d("Successfully updated user")
                true
            } else{
                Timber.e("Failed to update user ${r.body()}")
                 false
            }
        } catch (e: Exception) {
            Timber.e("Failed to RUN update user ${e.cause}")
             null
        }
    }

    suspend fun createUser(
        active: Boolean? = null,
        lat: Float? = null,
        lon: Float? = null,
        timeIntervalH: Int? = null,
        token: String? = null,
    ): Boolean? {
        if(appStateRepository.appState.first().isRegistered) return null
        val token = token ?: appStateRepository.getToken()
        Timber.d("Got token $token")
        return try {
            val r = client.createUser(userId, active, lat, lon, timeIntervalH, token)
            if(r.isSuccessful()) {
                appStateRepository.setRegistered(true)

                Timber.d("Successfully created user")
                 true
            } else {
                Timber.e("Failed to create user ${r.body()}")
                false
            }
        } catch (e: Exception) {
            Timber.e("Failed to run createUser ${e.cause}")
            null
        }
    }

    suspend fun getUser(): User? {
       return try {
            val r = client.getUser(userId)
            if(r.isSuccessful()){
                val user = r.body()
                user
            } else {
                Timber.e("Failed to get user ${r.body()}")
                null
            }
        } catch(e: Exception){
            Timber.e("Failed to fetch userData ${e.cause}")
            null
        }
    }

    suspend fun unregisterUser(): Boolean?{
        appStateRepository.setRegistered(false)
        return true
    }


}