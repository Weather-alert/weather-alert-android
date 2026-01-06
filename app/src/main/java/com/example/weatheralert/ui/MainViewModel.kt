package com.example.weatheralert.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.api.dataClass.LatLon
import com.example.weatheralert.api.dataClass.User
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.datastore.AppState
import com.example.weatheralert.datastore.AppStateRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val appStateRepository = AppStateRepository(application)

    val appState: LiveData<AppState> = appStateRepository.appState.asLiveData()
    private val _user = MutableLiveData<User>(User(0,"null",false, LatLon(0f,0f),0, token=""))
    val user: LiveData<User> = _user

    private suspend fun fetchCurrentSettings(){
        val user = NetworkModule.userService.getUser()
        if(user != null){
            _user.value = user
        }
    }
    fun registerUser(doneFunction: () -> Unit){
        viewModelScope.launch {
            val r = NetworkModule.userService.createUser()
            fetchCurrentSettings()
            doneFunction()
        }
    }
    fun unregisterUser(){
        viewModelScope.launch {
            val r = NetworkModule.userService.unregisterUser()
            fetchCurrentSettings()
        }

    }
    fun updateUser(doneFunction: () -> Unit, req: UserUpdateRequest){
        viewModelScope.launch {
            val r = NetworkModule.userService.updateUser(req)
            if(r == true){
                fetchCurrentSettings()
                doneFunction()
            }
        }
    }

    init {
        viewModelScope.launch {
            fetchCurrentSettings()
        }
    }
}