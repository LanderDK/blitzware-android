package com.example.blitzware_android.data

import com.example.blitzware_android.model.Log
import com.example.blitzware_android.network.LogApiService

/**
 * Log repository
 *
 * @constructor Create empty Log repository
 */
interface LogRepository {
    /**
     * Get logs by username
     *
     * @param token
     * @param username
     * @return
     */
    suspend fun getLogsByUsername(
        token: String,
        username: String
    ): List<Log>

    /**
     * Delete log by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteLogById(
        token: String,
        id: Int
    )
}

/**
 * Network log repository
 *
 * @property logApiService
 * @constructor Create empty Network log repository
 */
class NetworkLogRepository(
    private val logApiService: LogApiService
) : LogRepository {
    override suspend fun getLogsByUsername(token: String, username: String): List<Log> {
        val authorizationHeader = "Bearer $token"
        return logApiService.getLogsByUsername(authorizationHeader, username)
    }

    override suspend fun deleteLogById(token: String, id: Int) {
        val authorizationHeader = "Bearer $token"
        logApiService.deleteLogById(authorizationHeader, id)
    }
}