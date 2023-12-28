package com.example.blitzware_android.network

import com.example.blitzware_android.model.ChatMessage
import com.example.blitzware_android.model.CreateChatMessageBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Chat message api service
 *
 * @constructor Create empty Chat message api service
 */
interface ChatMessageApiService {
    /**
     * Get chat msgs by chat id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @GET("chatMsgs/chat/{id}")
    suspend fun getChatMsgsByChatId(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): List<ChatMessage>

    /**
     * Create chat message
     *
     * @param authorizationHeader
     * @param body
     * @return
     */
    @POST("chatMsgs")
    suspend fun createChatMessage(
        @Header("Authorization") authorizationHeader: String,
        @Body body: CreateChatMessageBody
    ): ChatMessage

    /**
     * Delete chat message by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("chatMsgs/{id}")
    suspend fun deleteChatMessageById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: Int
    ): Response<Unit>
}