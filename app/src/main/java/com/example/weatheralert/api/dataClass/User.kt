package com.example.weatheralert.api.dataClass

data class UserUpdateRequest(
    var active: Boolean? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var timeIntervalH: Int? = null // default is an hour
)

data class User(
    var seqNum: Int,
    var id: String,
    var active: Boolean,
    var latLon: LatLon,
    var timeIntervalH: Int
)

data class LatLon(var lat: Float, var lon: Float)
