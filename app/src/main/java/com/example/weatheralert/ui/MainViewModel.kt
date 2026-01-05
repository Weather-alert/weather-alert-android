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
    private val _user = MutableLiveData<User>(User(0,"null",false, LatLon(0f,0f),0))
    val user: LiveData<User> = _user
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
                fetchCurrentSettings()
            } catch (e: Exception) {
                Timber.e("Failed to run createUser ${e.cause}")
            } finally {
                doneFunction()
            }
        }
    }
    private suspend fun fetchCurrentSettings(){
        try {
            val r = NetworkModule.userService.getUser(id = AppConfig.androidId)
            if(r.isSuccessful()){
                val tmpUser: User = r.body()!!
                _user.postValue(tmpUser)
                Timber.d("Successfully fetched user settings")
            } else {
                Timber.e("Failed to get user ${r.body()}")
            }
        } catch(e: Exception){
            Timber.e("Failed to fetch userData ${e.cause}")
        }
    }
    fun updateUser(doneFunction: () -> Unit, req: UserUpdateRequest){
        viewModelScope.launch {
            try {
                val r = NetworkModule.userService.updateUser(id = AppConfig.androidId, req)

                if (r.isSuccessful()) {
                    Timber.d("Successfully updated user")
                } else{
                    Timber.e("Failed to update user ${r.body()}")
                }
                fetchCurrentSettings()
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

            try{
                val r = NetworkModule.userService.getUser(AppConfig.androidId)
                if(r.isSuccessful()){
                    //already registered
                    setRegistered(true)
                    fetchCurrentSettings()
                } else {
                    setRegistered(false)
                }
            } catch (e: Exception){
                Timber.e("Can't run getUser ${e.cause}")
            }
            Timber.d("Registered: ${appState.value?.isRegistered}")
        }
    }
}