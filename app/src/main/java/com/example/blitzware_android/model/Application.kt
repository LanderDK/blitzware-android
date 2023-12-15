package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Application(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "secret")
    val secret: String,
    @SerialName(value = "status")
    var status: Int,
    @SerialName(value = "hwidCheck")
    var hwidCheck: Int,
    @SerialName(value = "developerMode")
    var developerMode: Int,
    @SerialName(value = "integrityCheck")
    var integrityCheck: Int,
    @SerialName(value = "freeMode")
    var freeMode: Int,
    @SerialName(value = "twoFactorAuth")
    var twoFactorAuth: Int,
    @SerialName(value = "programHash")
    var programHash: String?,
    @SerialName(value = "version")
    var version: String,
    @SerialName(value = "downloadLink")
    var downloadLink: String?,
    @SerialName(value = "adminRoleId")
    var adminRoleId: Int?,
    @SerialName(value = "adminRoleLevel")
    var adminRoleLevel: Int?,
    @SerialName(value = "account")
    val account: AccountOfApp,
)

@Serializable
data class AccountOfApp(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String
)

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