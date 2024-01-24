package com.example.blitzware_android.data

import com.example.blitzware_android.data.database.ApplicationDao
import com.example.blitzware_android.data.database.dbSelectedApplication
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateApplicationBody
import com.example.blitzware_android.model.UpdateApplicationBody
import com.example.blitzware_android.network.ApplicationApiService

/**
 * Application repository
 *
 * @constructor Create empty Application repository
 */
interface ApplicationRepository {
    /**
     * Get applications of account
     *
     * @param token
     * @param accountId
     * @return
     */
    suspend fun getApplicationsOfAccount(token: String, accountId: String): List<Application>

    /**
     * Get application by id
     *
     * @param token
     * @param id
     * @return
     */
    suspend fun getApplicationById(token: String, id: String): Application

    /**
     * Update application by id
     *
     * @param token
     * @param id
     * @param body
     */
    suspend fun updateApplicationById(
        token: String,
        id: String,
        body: UpdateApplicationBody
    )

    /**
     * Delete application by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteApplicationById(token: String, id: String)

    /**
     * Create application
     *
     * @param token
     * @param body
     * @return
     */
    suspend fun createApplication(token: String, body: CreateApplicationBody): Application


    /**
     * Get selected application stream
     *
     * @return
     */
    suspend fun getSelectedApplicationStream(): dbSelectedApplication

    /**
     * Insert selected application
     *
     * @param application
     */
    suspend fun insertSelectedApplication(application: dbSelectedApplication)

    /**
     * Delete selected application entry
     *
     */
    suspend fun deleteSelectedApplicationEntry()

    /**
     * Update selected application
     *
     * @param application
     */
    suspend fun updateSelectedApplication(application: dbSelectedApplication)
}

/**
 * Network application repository
 *
 * @property applicationDao
 * @property applicationApiService
 * @constructor Create empty Network application repository
 */
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

    override suspend fun getSelectedApplicationStream(): dbSelectedApplication {
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
