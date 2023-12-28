package com.example.blitzware_android.network

import com.example.blitzware_android.model.CreateUserBody
import com.example.blitzware_android.model.UpdateUserBody
import com.example.blitzware_android.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * User api service
 *
 * @constructor Create empty User api service
 */
interface UserApiService {
    /**
     * Get users of application
     *
     * @param authorizationHeader
     * @param applicationId
     * @return
     */
    @GET("users/{applicationId}")
    suspend fun getUsersOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<User>

    /**
     * Create user from dashboard
     *
     * @param authorizationHeader
     * @param body
     * @return
     */
    @POST("users/registerFromDashboard")
    suspend fun createUserFromDashboard(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateUserBody
    ): User

    /**
     * Update user by id
     *
     * @param authorizationHeader
     * @param id
     * @param body
     * @return
     */
    @PUT("users/{id}")
    suspend fun updateUserById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateUserBody
    ): Response<Unit>

    /**
     * Delete user by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("users/{id}")
    suspend fun deleteUserById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}