package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppLog(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "username")
    var username: String,
    @SerialName(value = "date")
    var date: String,
    @SerialName(value = "action")
    var action: String,
    @SerialName(value = "ip")
    var ip: String,
    @SerialName(value = "appId")
    var appId: String,
)