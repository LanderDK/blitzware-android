package com.example.blitzware_android.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.blitzware_android.BlitzWareApplication
import com.example.blitzware_android.data.AccountRepository
import com.example.blitzware_android.data.ChatMessageRepository
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.ChatMessage
import com.example.blitzware_android.model.CreateChatMessageBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime

sealed interface ChatMessageUiState {
    data class Success(val apps: List<ChatMessage>) : ChatMessageUiState
    data class Error(val code: String, val message: String) : ChatMessageUiState
    object Loading : ChatMessageUiState
}

class ChatMessageViewModel(
    private val chatMessageRepository: ChatMessageRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var chatMessageUiState: ChatMessageUiState by mutableStateOf(ChatMessageUiState.Loading)
        private set

    private val _chatMsgs = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMsgs: StateFlow<List<ChatMessage>> get() = _chatMsgs

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> get() = _account

    init {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) {
                accountRepository.getAccountStream()
            }
            _account.value = account
            getChatMsgsByChatId(1)
        }
    }

    fun getChatMsgsByChatId(chatId: Int) {
        viewModelScope.launch {
            chatMessageUiState = ChatMessageUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val chatMsgs = chatMessageRepository.getChatMsgsByChatId(token, chatId)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                chatMessageUiState = ChatMessageUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun deleteChatMessageById(chatMsg: ChatMessage) {
        viewModelScope.launch {
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                chatMessageRepository.deleteChatMessageById(token, chatMsg.id)
                val chatMsgs = _chatMsgs.value.toMutableList()
                chatMsgs.remove(chatMsg)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                chatMessageUiState = ChatMessageUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun createChatMessage(msg: String, chatId: Int) {
        viewModelScope.launch {
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val currentDateTime = LocalDateTime.now()
                val currentDateTimeString = currentDateTime.toString()
                val body = CreateChatMessageBody(
                    username = account.value?.account?.username ?: throw Exception("Username is null"),
                    message = msg,
                    date = currentDateTimeString,
                    chatId = chatId
                )
                val chatMsg = chatMessageRepository.createChatMessage(token, body)
                val chatMsgs = _chatMsgs.value.toMutableList()
                chatMsgs.add(chatMsg)
                _chatMsgs.value = chatMsgs
                chatMessageUiState = ChatMessageUiState.Success(chatMsgs)
            } catch (e: IOException) {
                Log.d("ChatMessageViewModel", "IOException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ChatMessageViewModel", "HttpException")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                chatMessageUiState = ChatMessageUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ChatMessageViewModel", "Exception")
                Log.d("ChatMessageViewModel", e.message.toString())
                Log.d("ChatMessageViewModel", e.stackTraceToString())
                chatMessageUiState = ChatMessageUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [ChatMessageViewModel] that takes [ChatMessageRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val chatMessageRepository = application.container.chatMessageRepository
                val accountRepository = application.container.accountRepository
                ChatMessageViewModel(
                    chatMessageRepository = chatMessageRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}