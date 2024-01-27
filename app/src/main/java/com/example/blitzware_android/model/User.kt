package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User
 *
 * @property id
 * @property username
 * @property email
 * @property expiryDate
 * @property lastLogin
 * @property lastIP
 * @property hwid
 * @property license
 * @property enabled
 * @property twoFactorAuth
 * @property twoFactorAuthSecret
 * @property userSubId
 * @property userSubLevel
 * @property application
 * @constructor Create empty User
 */
@Serializable
data class User(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "username")
    var username: String,
    @SerialName(value = "email")
    var email: String,
    @SerialName(value = "expiryDate")
    var expiryDate: String,
    @SerialName(value = "lastLogin")
    var lastLogin: String,
    @SerialName(value = "lastIP")
    var lastIP: String,
    @SerialName(value = "hwid")
    var hwid: String,
    @SerialName(value = "license")
    var license: String,
    @SerialName(value = "enabled")
    var enabled: Int,
    @SerialName(value = "twoFactorAuth")
    var twoFactorAuth: Int,
    @SerialName(value = "twoFactorAuthSecret")
    var twoFactorAuthSecret: String?,
    @SerialName(value = "userSubId")
    var userSubId: Int?,
    @SerialName(value = "userSubLevel")
    var userSubLevel: Int?,
    @SerialName(value = "application")
    var application: ApplicationUser
)

/**
 * Application user
 *
 * @property id
 * @property name
 * @constructor Create empty Application user
 */
@Serializable
data class ApplicationUser(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String
)

/**
 * Create user body
 *
 * @property username
 * @property email
 * @property password
 * @property applicationId
 * @property expiry
 * @property subscription
 * @constructor Create empty Create user body
 */
@Serializable
data class CreateUserBody(
    @SerialName(value = "username") val username: String,
    @SerialName(value = "email") val email: String,
    @SerialName(value = "password") val password: String,
    @SerialName(value = "applicationId") val applicationId: String,
    @SerialName(value = "expiry") val expiry: String,
    @SerialName(value = "subscription") val subscription: Int,
)

/**
 * Update user body
 *
 * @property username
 * @property email
 * @property expiryDate
 * @property hwid
 * @property twoFactorAuth
 * @property enabled
 * @property subscription
 * @constructor Create empty Update user body
 */
@Serializable
data class UpdateUserBody(
    @SerialName(value = "username") val username: String,
    @SerialName(value = "email") val email: String,
    @SerialName(value = "expiryDate") val expiryDate: String,
    @SerialName(value = "hwid") val hwid: String,
    @SerialName(value = "twoFactorAuth") val twoFactorAuth: Int,
    @SerialName(value = "enabled") val enabled: Int,
    @SerialName(value = "subscription") val subscription: Int?
)