package com.example.blitzware_android.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blitzware_android.data.DefaultAppContainer
import com.example.blitzware_android.model.ChatMessage
import com.example.blitzware_android.network.CreateChatMessageBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime

sealed interface ChatMessageUiState {
    data class Success(val apps: List<ChatMessage>) : ChatMessageUiState
    object Error : ChatMessageUiState
    object Loading : ChatMessageUiState
}

class ChatMessageViewModel(private val accountViewModel: AccountViewModel) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var chatMessageUiState: ChatMessageUiState by mutableStateOf(ChatMessageUiState.Loading)
        private set

    private val _chatMsgs = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMsgs: StateFlow<List<ChatMessage>> get() = _chatMsgs

    init {
        getChatMsgsByChatId(1)
    }

    fun getChatMsgsByChatId(chatId: Int) {
        viewModelScope.launch {
            chatMessageUiState = ChatMessageUiState.Loading
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val chatMsgs = DefaultAppContainer().chatMessageRepository.getChatMsgsByChatId(token, chatId)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: HttpException) {
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            }
        }
    }

    fun deleteChatMessageById(chatMsg: ChatMessage) {
        viewModelScope.launch {
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                DefaultAppContainer().chatMessageRepository.deleteChatMessageById(token, chatMsg.id)
                val chatMsgs = _chatMsgs.value.toMutableList()
                chatMsgs.remove(chatMsg)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: HttpException) {
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            }
        }
    }

    fun createChatMessage(msg: String, chatId: Int) {
        viewModelScope.launch {
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val currentDateTime = LocalDateTime.now()
                val currentDateTimeString = currentDateTime.toString()
                val body = CreateChatMessageBody(
                    username = accountViewModel.account?.account?.username ?: throw Exception("Username is null"),
                    message = msg,
                    date = currentDateTimeString,
                    chatId = chatId
                )
                val chatMsg = DefaultAppContainer().chatMessageRepository.createChatMessage(token, body)
                val chatMsgs = _chatMsgs.value.toMutableList()
                chatMsgs.add(chatMsg)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: HttpException) {
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                chatMessageUiState = ChatMessageUiState.Error
            }
        }
    }
}
