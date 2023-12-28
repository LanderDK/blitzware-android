package com.example.blitzware_android.data

import com.example.blitzware_android.model.AppLog
import com.example.blitzware_android.network.AppLogApiService

/**
 * App log repository
 *
 * @constructor Create empty App log repository
 */
interface AppLogRepository {
    /**
     * Get app logs of application
     *
     * @param token
     * @param applicationId
     * @return
     */
    suspend fun getAppLogsOfApplication(
        token: String,
        applicationId: String
    ): List<AppLog>

    /**
     * Delete app log by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteAppLogById(
        token: String,
        id: Int
    )
}

/**
 * Network app log repository
 *
 * @property appLogApiService
 * @constructor Create empty Network app log repository
 */
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