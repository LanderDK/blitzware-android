package com.example.blitzware_android.network

import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData
import com.example.blitzware_android.model.UpdateAccountPicBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApiService {
    @POST("accounts/login")
    suspend fun login(@Body body: Map<String, String>): Account

    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): AccountData

    @PUT("accounts/profilePicture/{id}")
    suspend fun updateAccountProfilePictureById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateAccountPicBody
    ): Response<Unit>
}
