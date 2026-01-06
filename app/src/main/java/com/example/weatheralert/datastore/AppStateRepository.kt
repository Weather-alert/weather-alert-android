package com.example.weatheralert.datastore

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AppStateRepository(
    private val context: Context
) {

    private val dataStore = context.appStateDataStore

    val appState: Flow<AppState> = dataStore.data

    suspend fun initialize() {

        if (appState.first().isRegistered == false){
            dataStore.updateData { state ->
               state
            }
        }
    }

    suspend fun setAndroidId(id: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAndroidId(id)
                .build()
        }
    }

    suspend fun setRegistered(registered: Boolean) {
        dataStore.updateData {
            it.toBuilder()
                .setIsRegistered(registered)
                .build()
        }
    }

    suspend fun setToken(token: String){
        dataStore.updateData {
            it.toBuilder()
                .setToken(token)
                .build()
        }
    }
    fun fetchToken(){
        Firebase.messaging.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("Fetching FCM registration token failed")
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                CoroutineScope(Dispatchers.IO).launch {
                    setToken(token)

                    // Log
                    Timber.d("fetched token $token")
                }
            },
        )

    }
    suspend fun getToken(): String = appState.first().token
}
