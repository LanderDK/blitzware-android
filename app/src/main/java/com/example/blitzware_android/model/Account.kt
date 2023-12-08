package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    @SerialName(value = "account")
    val account: AccountData,
    @SerialName(value = "token")
    val token: String
)

@Serializable
data class AccountData(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "username")
    var username: String,
    @SerialName(value = "email")
    var email: String,
    @SerialName(value = "roles")
    var roles: List<String>,
    @SerialName(value = "creationDate")
    var creationDate: String,
    @SerialName(value = "profilePicture")
    var profilePicture: String?,
    @SerialName(value = "emailVerified")
    var emailVerified: Int,
    @SerialName(value = "twoFactorAuth")
    var twoFactorAuth: Int,
    @SerialName(value = "enabled")
    var enabled: Int
)