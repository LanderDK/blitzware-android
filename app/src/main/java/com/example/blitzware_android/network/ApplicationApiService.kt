package com.example.blitzware_android.network

import com.example.blitzware_android.model.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
@Serializable
data class UpdateApplicationBody(
    @SerialName(value = "status") val status: Int,
    @SerialName(value = "hwidCheck") val hwidCheck: Int,
    @SerialName(value = "developerMode") val developerMode: Int,
    @SerialName(value = "integrityCheck") val integrityCheck: Int,
    @SerialName(value = "freeMode") val freeMode: Int,
    @SerialName(value = "twoFactorAuth") val twoFactorAuth: Int,
    @SerialName(value = "programHash") val programHash: String?,
    @SerialName(value = "version") val version: String,
    @SerialName(value = "downloadLink") val downloadLink: String?,
    @SerialName(value = "accountId") val accountId: String,
    @SerialName(value = "subscription") val subscription: Int?
)
@Serializable
data class CreateApplicationBody(
    @SerialName(value = "name") val name: String,
    @SerialName(value = "accountId") val accountId: String,
)

interface ApplicationApiService {
    @GET("applications/byAccId/{id}")
    suspend fun getApplicationsOfAccount(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): List<Application>

    @PUT("applications/{id}")
    suspend fun updateApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String,
        @Body body: UpdateApplicationBody
    ): Response<Unit>

    @DELETE("applications/{id}")
    suspend fun deleteApplicationById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>

    @POST("applications")
    suspend fun createApplication(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateApplicationBody
    ): Application
}
