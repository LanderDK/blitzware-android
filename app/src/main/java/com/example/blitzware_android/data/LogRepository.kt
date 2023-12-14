package com.example.blitzware_android.data

import com.example.blitzware_android.model.Log
import com.example.blitzware_android.network.LogApiService

interface LogRepository {
    suspend fun getLogsByUsername(
        token: String,
        username: String
    ): List<Log>

    suspend fun deleteLogById(
        token: String,
        id: Int
    )
}

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