package com.example.weatheralert.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.api.dataClass.LatLon
import com.example.weatheralert.api.dataClass.User
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@SuppressLint("MissingPermission")
class LocationTrackWorker(appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams) {

        val locationClient = LocationServices.getFusedLocationProviderClient(appContext)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun doWork(): Result {
        if(!hasLocationPermission()){
            Timber.e("Doesn't have location permission")
            return Result.failure()
        }
        var lat = 0f
        var lon = 0f

        val loc = getCurrentLocationSuspending()

        if(loc == null) {
            Timber.e("Location fetching failed")
            return Result.retry()
        }
            lat = loc.latitude.toFloat()
            lon = loc.longitude.toFloat()

        val r = NetworkModule.userService.updateUser(UserUpdateRequest(latitude = lat, longitude = lon))
        if(r == true){
            Timber.d("Location update Successful latLon: $lat, $lon")
        } else {
            Timber.e("Location update Failed latLon $lat, $lon")
            return Result.failure()
        }
        return Result.success()
    }

    fun hasLocationPermission(): Boolean{
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission( applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    private suspend fun getCurrentLocationSuspending() =
        suspendCancellableCoroutine{ cont ->

            locationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            )
                .addOnSuccessListener { cont.resume(it) {} }
                .addOnFailureListener { cont.resume(null) {} }
        }
}