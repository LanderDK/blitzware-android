package com.example.blitzware_android.network

import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.model.UserSubBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserSubApiService {
    @GET("userSubs/application/{applicationId}")
    suspend fun getUserSubsOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<UserSub>

    @POST("userSubs")
    suspend fun createUserSub(
        @Header("Authorization") authorizationHeader: String,
        @Body body: UserSubBody
    ): UserSub

    @PUT("userSubs/{id}")
    suspend fun updateUserSubById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UserSubBody
    ): Response<Unit>

    @DELETE("userSubs/{id}")
    suspend fun deleteUserSubById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}