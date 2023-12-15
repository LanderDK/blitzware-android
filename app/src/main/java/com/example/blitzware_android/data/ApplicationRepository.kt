package com.example.blitzware_android.data

import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateApplicationBody
import com.example.blitzware_android.model.UpdateApplicationBody
import com.example.blitzware_android.network.ApplicationApiService

interface ApplicationRepository {
    suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application>

    suspend fun getApplicationById(token: String, id: String): Application

    suspend fun updateApplicationById(
        token: String,
        id: String,
        body: UpdateApplicationBody
    )

    suspend fun deleteApplicationById(token: String, id: String)

    suspend fun createApplication(token: String, body: CreateApplicationBody): Application
}

class NetworkApplicationRepository(
    private val applicationApiService: ApplicationApiService
) : ApplicationRepository {
    override suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application> {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.getApplicationsOfAccount(authorizationHeader, accountId)
    }

    override suspend fun getApplicationById(token: String, id: String): Application {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.getApplicationById(authorizationHeader, id)
    }

    override suspend fun updateApplicationById(
        token: String,
        id: String,
        body: UpdateApplicationBody
    ) {
        val authorizationHeader = "Bearer $token"
        applicationApiService.updateApplicationById(authorizationHeader, id, body)
    }

    override suspend fun deleteApplicationById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        applicationApiService.deleteApplicationById(authorizationHeader, id)
    }

    override suspend fun createApplication(
        token: String,
        body: CreateApplicationBody
    ): Application {
        val authorizationHeader = "Bearer $token"
        return applicationApiService.createApplication(authorizationHeader, body)
    }
}
