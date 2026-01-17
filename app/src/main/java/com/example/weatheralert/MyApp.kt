package com.example.weatheralert

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.weatheralert.Log.MyDebugTree
import com.example.weatheralert.configs.AppContextHolder
import com.example.weatheralert.datastore.AppStateRepository
import com.example.weatheralert.managers.LocationTrackWorker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.notification_channel_id)
            val channelName = getString(R.string.notification_channel_name)

            val channel =
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "Wakes screen on alert"
                    enableLights(true)
                    lightColor = 1
                    enableVibration(true)
                }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        AppStateRepository(this).fetchToken()

        AppContextHolder.context = applicationContext
        Timber.plant(MyDebugTree(this))

        uploadWorker()
    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    fun uploadWorker(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val uploadWorker = PeriodicWorkRequest.Builder(
            LocationTrackWorker::class.java,
            5,
            TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(uploadWorker)
    }
}
