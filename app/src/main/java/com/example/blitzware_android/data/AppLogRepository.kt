package com.example.blitzware_android.data

import com.example.blitzware_android.model.AppLog
import com.example.blitzware_android.network.AppLogApiService

interface AppLogRepository {
    suspend fun getAppLogsOfApplication(
        token: String,
        applicationId: String
    ): List<AppLog>

    suspend fun deleteAppLogById(
        token: String,
        id: Int
    )
}

class NetworkAppLogRepository(
    private val appLogApiService: AppLogApiService
) : AppLogRepository {
    override suspend fun getAppLogsOfApplication(
        token: String,
        applicationId: String
    ): List<AppLog> {
        val authorizationHeader = "Bearer $token"
        return appLogApiService.getAppLogsOfApplication(authorizationHeader, applicationId)
    }

    override suspend fun deleteAppLogById(token: String, id: Int) {
        val authorizationHeader = "Bearer $token"
        appLogApiService.deleteAppLogById(authorizationHeader, id)
    }

}