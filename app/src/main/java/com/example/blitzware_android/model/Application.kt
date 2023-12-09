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