package com.example.blitzware_android.network

import com.example.blitzware_android.model.CreateLicenseBody
import com.example.blitzware_android.model.License
import com.example.blitzware_android.model.UpdateLicenseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LicenseApiService {
    @GET("licenses/{applicationId}")
    suspend fun getLicensesOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<License>

    @POST("licenses")
    suspend fun createLicense(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateLicenseBody
    ): List<License>

    @PUT("licenses/{id}")
    suspend fun updateLicenseById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateLicenseBody
    ): Response<Unit>

    @DELETE("licenses/{id}")
    suspend fun deleteLicenseById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}