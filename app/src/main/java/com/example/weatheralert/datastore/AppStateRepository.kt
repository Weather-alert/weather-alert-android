package com.example.weatheralert.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppStateRepository(
    private val context: Context
) {

    private val dataStore = context.appStateDataStore

    val appState: Flow<AppState> = dataStore.data

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

    suspend fun updateLastSync(timestamp: Long) {
        dataStore.updateData {
            it.toBuilder()
                .setLastSync(timestamp)
                .build()

        }
    }
}
