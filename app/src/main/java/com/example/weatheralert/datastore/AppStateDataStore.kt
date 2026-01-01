package com.example.weatheralert.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

private const val DATASTORE_FILE_NAME = "app_state.pb"

val Context.appStateDataStore: DataStore<AppState> by dataStore(
    fileName = DATASTORE_FILE_NAME,
    serializer = AppStateSerializer
)