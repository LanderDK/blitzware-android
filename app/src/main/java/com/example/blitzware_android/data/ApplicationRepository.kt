package com.example.blitzware_android.data

import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.network.ApplicationApiService

interface ApplicationRepository {
    suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application>
}

class NetworkApplicationRepository(
    private val applicationApiService: ApplicationApiService
) : ApplicationRepository {
    override suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application> {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.getApplicationsOfAccount(authorizationHeader, accountId)
    }
}
