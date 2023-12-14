package com.example.blitzware_android.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blitzware_android.data.DefaultAppContainer
import com.example.blitzware_android.model.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LogUiState {
    data class Success(val logs: List<Log>) : LogUiState
    object Error : LogUiState
    object Loading : LogUiState
}

class LogViewModel(private val accountViewModel: AccountViewModel) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var logUiState: LogUiState by mutableStateOf(LogUiState.Loading)
        private set

    private val _logs = MutableStateFlow<List<Log>>(emptyList())
    val logs: StateFlow<List<Log>> get() = _logs

    init {
        getLogsByUsername()
    }

    private fun getLogsByUsername() {
        viewModelScope.launch {
            logUiState = LogUiState.Loading
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val username = accountViewModel.account?.account?.username ?: throw Exception("Username is null")
                val logs = DefaultAppContainer().logRepository.getLogsByUsername(token, username)
                _logs.value = logs
                logUiState = LogUiState.Success(logs)
            } catch (e: IOException) {
                android.util.Log.d("LogViewModel", "IOException")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            } catch (e: HttpException) {
                android.util.Log.d("LogViewModel", "HttpException")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            } catch (e: Exception) {
                android.util.Log.d("LogViewModel", "Exception")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            }
        }
    }

    fun deleteLogById(log: Log) {
        viewModelScope.launch {
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                DefaultAppContainer().logRepository.deleteLogById(token, log.id)
                val logs = _logs.value.toMutableList()
                logs.remove(log)
                _logs.value = logs
                logUiState = LogUiState.Success(logs)
            } catch (e: IOException) {
                android.util.Log.d("LogViewModel", "IOException")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            } catch (e: HttpException) {
                android.util.Log.d("LogViewModel", "HttpException")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            } catch (e: Exception) {
                android.util.Log.d("LogViewModel", "Exception")
                android.util.Log.d("LogViewModel", e.message.toString())
                logUiState = LogUiState.Error
            }
        }
    }
}
