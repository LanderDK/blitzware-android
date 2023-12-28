package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * File
 *
 * @property id
 * @property name
 * @property size
 * @property createdOn
 * @property application
 * @constructor Create empty File
 */
@Serializable
data class File(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    var name: String,
    @SerialName(value = "size")
    var size: String,
    @SerialName(value = "createdOn")
    var createdOn: String,
    @SerialName(value = "application")
    var application: ApplicationFile
)

/**
 * Application file
 *
 * @property id
 * @property name
 * @constructor Create empty Application file
 */
@Serializable
data class ApplicationFile(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String
)