package com.example.blitzware_android.data

import com.example.blitzware_android.model.ChatMessage
import com.example.blitzware_android.model.CreateChatMessageBody
import com.example.blitzware_android.network.ChatMessageApiService

/**
 * Chat message repository
 *
 * @constructor Create empty Chat message repository
 */
interface ChatMessageRepository {
    /**
     * Get chat msgs by chat id
     *
     * @param token
     * @param id
     * @return
     */
    suspend fun getChatMsgsByChatId(
        token: String,
        id: Int
    ): List<ChatMessage>

    /**
     * Create chat message
     *
     * @param token
     * @param body
     * @return
     */
    suspend fun createChatMessage(
        token: String,
        body: CreateChatMessageBody
    ): ChatMessage

    /**
     * Delete chat message by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteChatMessageById(
        token: String,
        id: Int
    )
}

/**
 * Network chat message repository
 *
 * @property chatMessageApiService
 * @constructor Create empty Network chat message repository
 */
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
