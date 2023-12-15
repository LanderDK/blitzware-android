package com.example.blitzware_android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "username")
    var username: String,
    @SerialName(value = "message")
    var message: String,
    @SerialName(value = "date")
    var date: String,
    @SerialName(value = "chatId")
    val chatId: Int
)

@Serializable
data class CreateChatMessageBody(
    @SerialName(value = "username") val username: String,
    @SerialName(value = "message") val message: String,
    @SerialName(value = "date") val date: String,
    @SerialName(value = "chatId") val chatId: Int,
)