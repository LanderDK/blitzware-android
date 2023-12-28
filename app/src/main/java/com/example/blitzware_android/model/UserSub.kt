package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User sub
 *
 * @property id
 * @property name
 * @property level
 * @property applicationId
 * @constructor Create empty User sub
 */
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

/**
 * User sub body
 *
 * @property name
 * @property level
 * @property applicationId
 * @constructor Create empty User sub body
 */
@Serializable
data class UserSubBody(
    @SerialName(value = "name") val name: String,
    @SerialName(value = "level") val level: Int,
    @SerialName(value = "applicationId") val applicationId: String,
)