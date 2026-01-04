package com.example.weatheralert.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.application
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.datastore.AppState
import com.example.weatheralert.datastore.AppStateRepository
import com.example.weatheralert.datastore.appStateDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val appStateRepository = AppStateRepository(application)

    val appState: LiveData<AppState> = appStateRepository.appState.asLiveData()

    fun setRegistered(b: Boolean){
        viewModelScope.launch {
            appStateRepository.setRegistered(b)
        }
    }
    fun registerUser(doneFunction: () -> Unit){
        viewModelScope.launch {
            try {
                val r = NetworkModule.userService.createUser(id = AppConfig.androidId)

                if (r.isSuccessful()) {
                    setRegistered(true)
                    Timber.d("Successfully created user")
                } else {
                    Timber.e("Failed to create user ${r.body()}")
                }
            } catch (e: Exception) {
                Timber.e("Failed to run createUser ${e.cause}")
            } finally {
                doneFunction()
            }
        }
    }

    fun updateUser(doneFunction: () -> Unit, req: UserUpdateRequest){
        viewModelScope.launch {
            try {
                val r = NetworkModule.userService.updateUser(id = AppConfig.androidId, req)

                if (r.isSuccessful()) {
                    Timber.d("Successfully updated user")
                }
                else{
                    Timber.e("Failed to update user ${r.body()}")
                }
            } catch (e: Exception) {
                    Timber.e("Failed to RUN update user ${e.cause}")
            } finally {
                doneFunction()
            }
        }
    }
    init {
        viewModelScope.launch {
            appStateRepository.initialize()
        }
    }
}