package com.example.blitzware_android.network

import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData
import com.example.blitzware_android.model.UpdateAccountPicBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApiService {
    @POST("accounts/login")
    suspend fun login(@Body body: Map<String, String>): Account

    @POST("accounts/register")
    suspend fun register(@Body body: Map<String, String>)

    @POST("accounts/logout")
    suspend fun logout(@Header("Authorization") authorizationHeader: String)

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
    )

    @POST("accounts/verifyOTP")
    suspend fun verifyLoginOTP(@Body body: Map<String, String>): Account

    @POST("2fa/verify/login")
    suspend fun verifyLogin2FA(@Body body: Map<String, String>): Account
}
