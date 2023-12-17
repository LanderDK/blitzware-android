package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class License(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "license")
    var license: String,
    @SerialName(value = "days")
    var days: Int,
    @SerialName(value = "expiryDate")
    var expiryDate: String,
    @SerialName(value = "used")
    var used: Int,
    @SerialName(value = "usedBy")
    var usedBy: String?,
    @SerialName(value = "enabled")
    var enabled: Int,
    @SerialName(value = "userSubId")
    var userSubId: Int?,
    @SerialName(value = "userSubLevel")
    var userSubLevel: Int?,
    @SerialName(value = "application")
    var application: ApplicationLicense
)

@Serializable
data class ApplicationLicense(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String
)

@Serializable
data class CreateLicenseBody(
    @SerialName(value = "days") val days: Int,
    @SerialName(value = "format") val format: String,
    @SerialName(value = "amount") val amount: Int,
    @SerialName(value = "subscription") val subscription: Int,
    @SerialName(value = "applicationId") val applicationId: String,
)

@Serializable
data class UpdateLicenseBody(
    @SerialName(value = "license") val license: String,
    @SerialName(value = "days") val days: Int,
    @SerialName(value = "used") val used: Int,
    @SerialName(value = "enabled") val enabled: Int,
    @SerialName(value = "subscription") val subscription: Int?
)