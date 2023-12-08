package com.example.blitzware_android.network

import com.example.blitzware_android.model.Account
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApiService {
    @POST("accounts/login")
    suspend fun login(@Body body: Map<String, String>): Account
}
