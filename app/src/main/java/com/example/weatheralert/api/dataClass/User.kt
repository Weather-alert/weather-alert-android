package com.example.weatheralert.api.dataClass

data class UserUpdateRequest(
    var active: Boolean? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var timeIntervalH: Int? = null,
    var token: String? = null,
)

data class User(
    var seqNum: Int,
    var id: String,
    var active: Boolean,
    var latLon: LatLon,
    var timeIntervalH: Int,
    var token: String,
)

data class LatLon(var lat: Float, var lon: Float)
