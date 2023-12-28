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

/**
 * Application api service
 *
 * @constructor Create empty Application api service
 */
interface ApplicationApiService {
    /**
     * Get applications of account
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @GET("applications/byAccId/{id}")
    suspend fun getApplicationsOfAccount(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): List<Application>

    /**
     * Get application by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @GET("applications/byAppId/{id}")
    suspend fun getApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Application

    /**
     * Create application
     *
     * @param authorizationHeader
     * @param body
     * @return
     */
    @POST("applications")
    suspend fun createApplication(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateApplicationBody
    ): Application

    /**
     * Update application by id
     *
     * @param authorizationHeader
     * @param id
     * @param body
     * @return
     */
    @PUT("applications/{id}")
    suspend fun updateApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateApplicationBody
    ): Response<Unit>

    /**
     * Delete application by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("applications/{id}")
    suspend fun deleteApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}
