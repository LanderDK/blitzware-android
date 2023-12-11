package com.example.blitzware_android.data

import com.example.blitzware_android.model.Application
import com.example.blitzware_android.network.ApplicationApiService
import com.example.blitzware_android.network.UpdateApplicationBody

interface ApplicationRepository {
    suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application>

    suspend fun updateApplicationById(
        token: String,
        id: String,
        body: UpdateApplicationBody
    ): Application

    suspend fun deleteApplicationById(token: String, id: String)
}

class NetworkApplicationRepository(
    private val applicationApiService: ApplicationApiService
) : ApplicationRepository {
    override suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application> {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.getApplicationsOfAccount(authorizationHeader, accountId)
    }

    override suspend fun updateApplicationById(
        token: String,
        id: String,
        body: UpdateApplicationBody
    ): Application {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.updateApplicationById(authorizationHeader, id, body)
    }

    override suspend fun deleteApplicationById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        applicationApiService.deleteApplicationById(authorizationHeader, id)
    }
}
