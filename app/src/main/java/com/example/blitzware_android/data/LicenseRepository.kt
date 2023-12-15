package com.example.blitzware_android.data

import com.example.blitzware_android.model.CreateLicenseBody
import com.example.blitzware_android.model.License
import com.example.blitzware_android.model.UpdateLicenseBody
import com.example.blitzware_android.network.LicenseApiService

interface LicenseRepository {
    suspend fun getLicensesOfApplication(
        token: String,
        applicationId: String
    ): List<License>

    suspend fun createLicense(
        token: String,
        body: CreateLicenseBody
    ): List<License>

    suspend fun updateLicenseById(
        token: String,
        id: String,
        body: UpdateLicenseBody
    )

    suspend fun deleteLicenseById(
        token: String,
        id: String
    )
}

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