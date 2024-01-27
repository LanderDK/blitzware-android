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

/**
 * Account api service
 *
 * @constructor Create empty Account api service
 */
interface AccountApiService {
    /**
     * Login
     *
     * @param body
     * @return
     */
    @POST("accounts/login")
    suspend fun login(@Body body: Map<String, String>): Account

    /**
     * Register
     *
     * @param body
     */
    @POST("accounts/register")
    suspend fun register(@Body body: Map<String, String>)

    /**
     * Logout
     *
     * @param authorizationHeader
     */
    @POST("accounts/logout")
    suspend fun logout(@Header("Authorization") authorizationHeader: String)

    /**
     * Get account by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): AccountData

    /**
     * Update account profile picture by id
     *
     * @param authorizationHeader
     * @param id
     * @param body
     */
    @PUT("accounts/profilePicture/{id}")
    suspend fun updateAccountProfilePictureById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateAccountPicBody
    ): Response<Unit>

    /**
     * Verify login o t p
     *
     * @param body
     * @return
     */
    @POST("accounts/verifyOTP")
    suspend fun verifyLoginOTP(@Body body: Map<String, String>): Account

    /**
     * Verify login2f a
     *
     * @param body
     * @return
     */
    @POST("2fa/verify/login")
    suspend fun verifyLogin2FA(@Body body: Map<String, String>): Account
}
