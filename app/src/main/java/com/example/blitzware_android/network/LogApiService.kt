package com.example.blitzware_android.network

import com.example.blitzware_android.model.Log
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface LogApiService {
    @GET("logs/{username}")
    suspend fun getLogsByUsername(
        @Header("Authorization") authorizationHeader: String,
        @Path("username") username: String
    ): List<Log>

    @DELETE("logs/{id}")
    suspend fun deleteLogById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}