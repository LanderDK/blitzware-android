package com.example.blitzware_android.data

import com.example.blitzware_android.model.CreateLicenseBody
import com.example.blitzware_android.model.License
import com.example.blitzware_android.model.UpdateLicenseBody
import com.example.blitzware_android.network.LicenseApiService

/**
 * License repository
 *
 * @constructor Create empty License repository
 */
interface LicenseRepository {
    /**
     * Get licenses of application
     *
     * @param token
     * @param applicationId
     * @return
     */
    suspend fun getLicensesOfApplication(
        token: String,
        applicationId: String
    ): List<License>

    /**
     * Create license
     *
     * @param token
     * @param body
     * @return
     */
    suspend fun createLicense(
        token: String,
        body: CreateLicenseBody
    ): List<License>

    /**
     * Update license by id
     *
     * @param token
     * @param id
     * @param body
     */
    suspend fun updateLicenseById(
        token: String,
        id: String,
        body: UpdateLicenseBody
    )

    /**
     * Delete license by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteLicenseById(
        token: String,
        id: String
    )
}

/**
 * Network license repository
 *
 * @property licenseApiService
 * @constructor Create empty Network license repository
 */
class NetworkLicenseRepository(
    private val licenseApiService: LicenseApiService
) : LicenseRepository {
    override suspend fun getLicensesOfApplication(
        token: String,
        applicationId: String
    ): List<License> {
        val authorizationHeader = "Bearer $token"
        return licenseApiService.getLicensesOfApplication(authorizationHeader, applicationId)
    }

    override suspend fun createLicense(token: String, body: CreateLicenseBody): List<License> {
        val authorizationHeader = "Bearer $token"
        return licenseApiService.createLicense(authorizationHeader, body)
    }

    override suspend fun updateLicenseById(token: String, id: String, body: UpdateLicenseBody) {
        val authorizationHeader = "Bearer $token"
        licenseApiService.updateLicenseById(authorizationHeader, id, body)
    }

    override suspend fun deleteLicenseById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        licenseApiService.deleteLicenseById(authorizationHeader, id)
    }

}