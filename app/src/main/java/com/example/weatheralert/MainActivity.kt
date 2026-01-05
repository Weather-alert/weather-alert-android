package com.example.weatheralert

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import com.example.weatheralert.ui.Navigation
import com.example.weatheralert.ui.theme.WeatherAlertTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.d("notifications permission granted")
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Timber.d("FCM can't post notifications wihtout post_notifications permissions")
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAlertTheme {


                // If a notification message is tapped, any data accompanying the notification
                // message is available in the intent extras. In this sample the launcher
                // intent is fired when the notification is tapped, so any accompanying data would
                // be handled here. If you want a different intent fired, set the click_action
                // field of the notification message to the desired intent. The launcher intent
                // is used when no click_action is specified.
                //
                // Handle possible data accompanying notification message.
                // [START handle_data_extras]
                intent.extras?.let {
                    for (key in it.keySet()) {
                        val value = intent.extras?.getString(key)
                        Timber.d("Key: $key Value: $value")
                    }
                }
                // [END handle_data_extras]
                var msg = ""
                Button({
                    Firebase.messaging.subscribeToTopic("weather")
                        .addOnCompleteListener { task ->
                            msg = "Subscribed"
                            if (!task.isSuccessful) {
                                msg = "Failed to subscribe"
                            }
                            Timber.d(msg)
                        }

                }) {
                    Text(msg)
                }
                var msg2 = ""
                Button({
                    Firebase.messaging.token.addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Timber.w("Fetching FCM registration token failed")
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = task.result

                            // Log and toast
                            msg2 = "$token"
                            Timber.d(msg2)
                        },
                    )
                }){
                    Text(msg2)
                }

                askNotificationPermission()
            }
        }
    }
    */
    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAlertTheme {
                Navigation()
                askNotificationPermission()
            }
        }
    }
}
