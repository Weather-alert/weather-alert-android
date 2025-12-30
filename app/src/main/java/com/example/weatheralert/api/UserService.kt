package com.example.weatheralert.api

import com.example.weatheralert.api.dataClass.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    // GET /users?id=...&name=... (optional filters)
    @GET("users")
    suspend fun getUsers(
        @Query("id") id: String? = null,
        @Query("name") name: String? = null,
        @Query("active") active: Boolean? = null,
        @Query("minLat") minLat: Float? = null,
        @Query("minLon") minLon: Float? = null,
        @Query("maxLat") maxLat: Float? = null,
        @Query("maxLon") maxLon: Float? = null,
        @Query("minNotify") minNotify: Long? = null
    ): Map<String, User>

    // GET /users/{id}
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): Response<User>

    // POST /users?name=...&active=...&lat=...&lon=...&timeIntervalH=...
    @POST("users")
    suspend fun createUser(
        @Query("id") id: String,
        @Query("active") active: Boolean? = null,
        @Query("lat") lat: Float? = null,
        @Query("lon") lon: Float? = null,
        @Query("timeIntervalH") timeIntervalH: Int? = null
    ): Response<Unit>

    // PATCH /users/{id} with JSON body
    @PATCH("users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body request: User
    ): Response<Unit>

    // DELETE /users/{id}
    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    ): Response<Unit>
}
