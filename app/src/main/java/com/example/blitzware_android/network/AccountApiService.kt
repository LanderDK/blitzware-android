package com.example.blitzware_android.network

import com.example.blitzware_android.model.Account
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {
    @POST("accounts/login")
    suspend fun login(@Body body: Map<String, String>): Account

    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Account
}
