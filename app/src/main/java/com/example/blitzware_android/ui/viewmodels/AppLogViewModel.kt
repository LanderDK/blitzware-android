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
import com.example.blitzware_android.data.AppLogRepository
import com.example.blitzware_android.data.ApplicationRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AppLog
import com.example.blitzware_android.model.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * App log ui state
 *
 * @constructor Create empty App log ui state
 */
sealed interface AppLogUiState {
    /**
     * Success
     *
     * @property appLogs
     * @constructor Create empty Success
     */
    data class Success(val appLogs: List<AppLog>) : AppLogUiState

    /**
     * Error
     *
     * @property code
     * @property message
     * @constructor Create empty Error
     */
    data class Error(val code: String, val message: String) : AppLogUiState
    object Loading : AppLogUiState
}

/**
 * App log view model
 *
 * @property appLogRepository
 * @property applicationRepository
 * @property accountRepository
 * @constructor Create empty App log view model
 */
class AppLogViewModel(
    private val appLogRepository: AppLogRepository,
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    var appLogUiState: AppLogUiState by mutableStateOf(AppLogUiState.Loading)

    private val _appLogs = MutableStateFlow<List<AppLog>>(emptyList())
    val appLogs: StateFlow<List<AppLog>> get() = _appLogs

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> get() = _account

    private val _application = MutableStateFlow<Application?>(null)
    val application: StateFlow<Application?> get() = _application

    init {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) {
                accountRepository.getAccountStream()
            }
            val application = withContext(Dispatchers.IO) {
                applicationRepository.getSelectedApplicationStream()
            }
            _account.value = account
            try {
                _application.value = application.asApplication(_account.value!!)
            } catch (e: Exception) {
                Log.d("AppLogViewModel", "Exception")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", e.stackTraceToString())
                appLogUiState = AppLogUiState.Error("Exception", e.message.toString())
            }
            getAppLogsOfApplication()
        }
    }

    private fun getAppLogsOfApplication() {
        viewModelScope.launch {
            appLogUiState = AppLogUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId =
                    application.value?.id ?: throw Exception("Application id is null")
                val appLogs = appLogRepository.getAppLogsOfApplication(token, applicationId)
                _appLogs.value = appLogs
                appLogUiState = AppLogUiState.Success(appLogs)
            } catch (e: IOException) {
                Log.d("AppLogViewModel", "IOException")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", e.stackTraceToString())
                appLogUiState = AppLogUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AppLogViewModel", "HttpException")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                appLogUiState = AppLogUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AppLogViewModel", "Exception")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", e.stackTraceToString())
                appLogUiState = AppLogUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Delete app log by id
     *
     * @param appLog
     */
    fun deleteAppLogById(appLog: AppLog) {
        viewModelScope.launch {
            appLogUiState = AppLogUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                appLogRepository.deleteAppLogById(token, appLog.id)
                val appLogs = _appLogs.value.toMutableList()
                appLogs.remove(appLog)
                _appLogs.value = appLogs
                appLogUiState = AppLogUiState.Success(appLogs)
            } catch (e: IOException) {
                Log.d("AppLogViewModel", "IOException")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", e.stackTraceToString())
                appLogUiState = AppLogUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AppLogViewModel", "HttpException")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                appLogUiState = AppLogUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AppLogViewModel", "Exception")
                Log.d("AppLogViewModel", e.message.toString())
                Log.d("AppLogViewModel", e.stackTraceToString())
                appLogUiState = AppLogUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [AppLogViewModel] that takes [AppLogRepository], [ApplicationRepository] and [AccountRepository] as a dependencies
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val appLogRepository = application.container.appLogRepository
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                AppLogViewModel(
                    appLogRepository = appLogRepository,
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}