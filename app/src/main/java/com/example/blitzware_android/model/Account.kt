package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    @SerialName(value = "account")
    var account: AccountData,
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

@Serializable
data class UpdateAccountPicBody(
    @SerialName(value = "profilePicture") val profilePicture: ProfilePicture,
)

@Serializable
data class ProfilePicture(
    @SerialName(value = "name") val name: String,
    @SerialName(value = "type") val type: String,
    @SerialName(value = "size") val size: Int,
    @SerialName(value = "dataURL") val dataURL: String,
)

fun UpdateAccountPicBody.toFormattedString(): String {
    return buildString {
        appendLine("{")
        appendLine("""  "name": "${profilePicture.name}",""")
        appendLine("""  "type": "${profilePicture.type}",""")
        appendLine("""  "size": ${profilePicture.size},""")
        appendLine("""  "dataURL": "${profilePicture.dataURL}" """)
        appendLine("}")
    }
}