package com.example.blitzware_android.data

import com.example.blitzware_android.model.ChatMessage
import com.example.blitzware_android.network.ChatMessageApiService
import com.example.blitzware_android.network.CreateChatMessageBody

interface ChatMessageRepository {
    suspend fun getChatMsgsByChatId(
        token: String,
        id: Int
    ): List<ChatMessage>

    suspend fun createChatMessage(
        token: String,
        body: CreateChatMessageBody
    ): ChatMessage

    suspend fun deleteChatMessageById(
        token: String,
        id: Int
    )
}

class NetworkChatMessageRepository(
    private val chatMessageApiService: ChatMessageApiService
) : ChatMessageRepository {
    override suspend fun getChatMsgsByChatId(
        token: String,
        id: Int
    ): List<ChatMessage> {
        val authorizationHeader = "Bearer $token"
        return chatMessageApiService.getChatMsgsByChatId(authorizationHeader, id)
    }

    override suspend fun createChatMessage(
        token: String,
        body: CreateChatMessageBody
    ): ChatMessage {
        val authorizationHeader = "Bearer $token"
        return chatMessageApiService.createChatMessage(authorizationHeader, body)
    }

    override suspend fun deleteChatMessageById(
        token: String,
        id: Int
    ) {
        val authorizationHeader = "Bearer $token"
        chatMessageApiService.deleteChatMessageById(authorizationHeader, id)
    }
}
