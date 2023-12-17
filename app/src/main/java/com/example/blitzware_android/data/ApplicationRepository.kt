package com.example.blitzware_android.data

import com.example.blitzware_android.data.database.ApplicationDao
import com.example.blitzware_android.data.database.dbSelectedApplication
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


    /**
     * Retrieve the selected application (only one will exist) from the given data source.
     */
    fun getSelectedApplicationStream(): dbSelectedApplication

    /**
     * Insert selected application in the data source
     */
    suspend fun insertSelectedApplication(application: dbSelectedApplication)

    /**
     * Delete selected application from the data source
     */
    suspend fun deleteSelectedApplicationEntry()

    /**
     * Update selected application in the data source
     */
    suspend fun updateSelectedApplication(application: dbSelectedApplication)
}

class NetworkApplicationRepository(
    private val applicationDao: ApplicationDao,
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

    override fun getSelectedApplicationStream(): dbSelectedApplication {
        return applicationDao.getSelectedApplication()
    }

    override suspend fun insertSelectedApplication(application: dbSelectedApplication) {
        applicationDao.insert(application)
    }

    override suspend fun deleteSelectedApplicationEntry() {
        applicationDao.deleteAll()
    }

    override suspend fun updateSelectedApplication(application: dbSelectedApplication) {
        applicationDao.update(application)
    }
}
