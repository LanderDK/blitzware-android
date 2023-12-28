package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * License
 *
 * @property id
 * @property license
 * @property days
 * @property expiryDate
 * @property used
 * @property usedBy
 * @property enabled
 * @property userSubId
 * @property userSubLevel
 * @property application
 * @constructor Create empty License
 */
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

/**
 * Application license
 *
 * @property id
 * @property name
 * @constructor Create empty Application license
 */
@Serializable
data class ApplicationLicense(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String
)

/**
 * Create license body
 *
 * @property days
 * @property format
 * @property amount
 * @property subscription
 * @property applicationId
 * @constructor Create empty Create license body
 */
@Serializable
data class CreateLicenseBody(
    @SerialName(value = "days") val days: Int,
    @SerialName(value = "format") val format: String,
    @SerialName(value = "amount") val amount: Int,
    @SerialName(value = "subscription") val subscription: Int,
    @SerialName(value = "applicationId") val applicationId: String,
)

/**
 * Update license body
 *
 * @property license
 * @property days
 * @property used
 * @property enabled
 * @property subscription
 * @constructor Create empty Update license body
 */
@Serializable
data class UpdateLicenseBody(
    @SerialName(value = "license") val license: String,
    @SerialName(value = "days") val days: Int,
    @SerialName(value = "used") val used: Int,
    @SerialName(value = "enabled") val enabled: Int,
    @SerialName(value = "subscription") val subscription: Int?
)