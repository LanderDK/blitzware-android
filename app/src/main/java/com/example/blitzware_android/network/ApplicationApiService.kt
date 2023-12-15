package com.example.blitzware_android.network

import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateApplicationBody
import com.example.blitzware_android.model.UpdateApplicationBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApplicationApiService {
    @GET("applications/byAccId/{id}")
    suspend fun getApplicationsOfAccount(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): List<Application>

    @GET("applications/byAppId/{id}")
    suspend fun getApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Application

    @POST("applications")
    suspend fun createApplication(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateApplicationBody
    ): Application

    @PUT("applications/{id}")
    suspend fun updateApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateApplicationBody
    ): Response<Unit>

    @DELETE("applications/{id}")
    suspend fun deleteApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}
