package com.example.blitzware_android.network

import com.example.blitzware_android.model.Application
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApplicationApiService {
    @GET("applications/byAccId/{id}")
    suspend fun getApplicationsOfAccount(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): List<Application>
}
