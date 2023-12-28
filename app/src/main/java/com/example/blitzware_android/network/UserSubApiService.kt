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

/**
 * User sub api service
 *
 * @constructor Create empty User sub api service
 */
interface UserSubApiService {
    /**
     * Get user subs of application
     *
     * @param authorizationHeader
     * @param applicationId
     * @return
     */
    @GET("userSubs/application/{applicationId}")
    suspend fun getUserSubsOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<UserSub>

    /**
     * Create user sub
     *
     * @param authorizationHeader
     * @param body
     * @return
     */
    @POST("userSubs")
    suspend fun createUserSub(
        @Header("Authorization") authorizationHeader: String,
        @Body body: UserSubBody
    ): UserSub

    /**
     * Update user sub by id
     *
     * @param authorizationHeader
     * @param id
     * @param body
     * @return
     */
    @PUT("userSubs/{id}")
    suspend fun updateUserSubById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int,
        @Body body: UserSubBody
    ): Response<Unit>

    /**
     * Delete user sub by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("userSubs/{id}")
    suspend fun deleteUserSubById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}