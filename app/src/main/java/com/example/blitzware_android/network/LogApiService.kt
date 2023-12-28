package com.example.blitzware_android.network

import com.example.blitzware_android.model.Log
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Log api service
 *
 * @constructor Create empty Log api service
 */
interface LogApiService {
    /**
     * Get logs by username
     *
     * @param authorizationHeader
     * @param username
     * @return
     */
    @GET("logs/{username}")
    suspend fun getLogsByUsername(
        @Header("Authorization") authorizationHeader: String,
        @Path("username") username: String
    ): List<Log>

    /**
     * Delete log by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("logs/{id}")
    suspend fun deleteLogById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}