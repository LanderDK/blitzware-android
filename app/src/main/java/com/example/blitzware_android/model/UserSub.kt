package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSub(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "name")
    var name: String,
    @SerialName(value = "level")
    var level: Int,
    @SerialName(value = "applicationId")
    val applicationId: String
)

@Serializable
data class UserSubBody(
    @SerialName(value = "name") val name: String,
    @SerialName(value = "level") val level: Int,
    @SerialName(value = "applicationId") val applicationId: String,
)