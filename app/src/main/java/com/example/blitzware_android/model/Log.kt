package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Log
 *
 * @property id
 * @property username
 * @property date
 * @property action
 * @property message
 * @constructor Create empty Log
 */
@Serializable
data class Log(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "username")
    var username: String,
    @SerialName(value = "date")
    var date: String,
    @SerialName(value = "action")
    var action: String,
    @SerialName(value = "message")
    var message: String,
)