package com.example.blitzware_android.network

import com.example.blitzware_android.model.AppLog
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AppLogApiService {
    @GET("appLogs/{applicationId}")
    suspend fun getAppLogsOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<AppLog>

    @DELETE("appLogs/{id}")
    suspend fun deleteAppLogById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}