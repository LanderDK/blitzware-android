package com.example.blitzware_android.network

import com.example.blitzware_android.model.ChatMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class CreateChatMessageBody(
    @SerialName(value = "username") val username: String,
    @SerialName(value = "message") val message: String,
    @SerialName(value = "date") val date: String,
    @SerialName(value = "chatId") val chatId: Int,
)

interface ChatMessageApiService {
    @GET("chatMsgs/chat/{id}")
    suspend fun getChatMsgsByChatId(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): List<ChatMessage>

    @POST("chatMsgs")
    suspend fun createChatMessage(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateChatMessageBody
    ): ChatMessage

    @DELETE("chatMsgs/{id}")
    suspend fun deleteChatMessageById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}