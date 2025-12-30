package com.example.weatheralert

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableSharedFlow

object ViewModelData {
    val newLogs = MutableSharedFlow<Boolean>()
}