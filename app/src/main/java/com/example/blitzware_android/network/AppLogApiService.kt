package com.example.blitzware_android.network

import com.example.blitzware_android.model.AppLog
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * App log api service
 *
 * @constructor Create empty App log api service
 */
interface AppLogApiService {
    /**
     * Get app logs of application
     *
     * @param authorizationHeader
     * @param applicationId
     * @return
     */
    @GET("appLogs/{applicationId}")
    suspend fun getAppLogsOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<AppLog>

    /**
     * Delete app log by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("appLogs/{id}")
    suspend fun deleteAppLogById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}