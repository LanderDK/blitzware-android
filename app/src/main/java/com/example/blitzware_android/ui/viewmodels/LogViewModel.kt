package com.example.blitzware_android.ui.viewmodels

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
import com.example.blitzware_android.data.LogRepository
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

sealed interface LogUiState {
    data class Success(val logs: List<Log>) : LogUiState
    data class Error(val code: String, val message: String) : LogUiState
    object Loading : LogUiState
}

class LogViewModel(
    private val logRepository: LogRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var logUiState: LogUiState by mutableStateOf(LogUiState.Loading)
        private set

    private val _logs = MutableStateFlow<List<Log>>(emptyList())
    val logs: StateFlow<List<Log>> get() = _logs

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> get() = _account

    init {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) {
                accountRepository.getAccountStream()
            }
            _account.value = account
            getLogsByUsername()
        }
    }

    private fun getLogsByUsername() {
        viewModelScope.launch {
            logUiState = LogUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val username = account.value?.account?.username ?: throw Exception("Username is null")
                val logs = logRepository.getLogsByUsername(token, username)
                _logs.value = logs
                logUiState = LogUiState.Success(logs)
            } catch (e: IOException) {
                android.util.Log.d("LogViewModel", "IOException")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", e.stackTraceToString())
                logUiState = LogUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                android.util.Log.d("LogViewModel", "HttpException")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                logUiState = LogUiState.Error(code, message)
            } catch (e: Exception) {
                android.util.Log.d("LogViewModel", "Exception")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", e.stackTraceToString())
                logUiState = LogUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun deleteLogById(log: Log) {
        viewModelScope.launch {
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                logRepository.deleteLogById(token, log.id)
                val logs = _logs.value.toMutableList()
                logs.remove(log)
                _logs.value = logs
                logUiState = LogUiState.Success(logs)
            } catch (e: IOException) {
                android.util.Log.d("LogViewModel", "IOException")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", e.stackTraceToString())
                logUiState = LogUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                android.util.Log.d("LogViewModel", "HttpException")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                logUiState = LogUiState.Error(code, message)
            } catch (e: Exception) {
                android.util.Log.d("LogViewModel", "Exception")
                android.util.Log.d("LogViewModel", e.message.toString())
                android.util.Log.d("LogViewModel", e.stackTraceToString())
                logUiState = LogUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [LogViewModel] that takes [LogRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val logRepository = application.container.logRepository
                val accountRepository = application.container.accountRepository
                LogViewModel(
                    logRepository = logRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}