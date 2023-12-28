package com.example.blitzware_android.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.blitzware_android.BlitzWareApplication
import com.example.blitzware_android.data.AccountRepository
import com.example.blitzware_android.data.ApplicationRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.data.database.asDbSelectedApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateApplicationBody
import com.example.blitzware_android.model.UpdateApplicationBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

sealed interface ApplicationUiState {
    data class Success(val apps: List<Application>) : ApplicationUiState
    data class Error(val code: String, val message: String) : ApplicationUiState
    object Loading : ApplicationUiState
}

class ApplicationViewModel(
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var applicationUiState: ApplicationUiState by mutableStateOf(ApplicationUiState.Loading)
        private set

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> get() = _applications


    private val _application = MutableStateFlow<Application?>(null)
    val application: StateFlow<Application?> get() = _application

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> get() = _account

    init {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) {
                accountRepository.getAccountStream()
            }
            _account.value = account
            getApplicationsOfAccount()
        }
    }

    private fun getApplicationsOfAccount() {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val accountId = account.value?.account?.id ?: throw Exception("Account id is null")
                val apps = applicationRepository.getApplicationsOfAccount(token, accountId)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                applicationUiState = ApplicationUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun getApplicationById(application: Application) {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val app = applicationRepository.getApplicationById(
                    token,
                    application.id
                )
                //applicationRepository.deleteSelectedApplicationEntry()
                applicationRepository.insertSelectedApplication(app.asDbSelectedApplication())
                applicationUiState = ApplicationUiState.Success(_applications.value)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                applicationUiState = ApplicationUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun getSelectedApplication() {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            _application.value = null
            try {
                val app = withContext(Dispatchers.IO) {
                    applicationRepository.getSelectedApplicationStream()
                }
                _application.value = app.asApplication(account.value!!)
                applicationUiState = ApplicationUiState.Success(_applications.value)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun updateApplicationById(application: Application) {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val body = UpdateApplicationBody(
                    status = application.status,
                    hwidCheck = application.hwidCheck,
                    developerMode = application.developerMode,
                    integrityCheck = application.integrityCheck,
                    freeMode = application.freeMode,
                    twoFactorAuth = application.twoFactorAuth,
                    programHash = application.programHash,
                    version = application.version,
                    downloadLink = application.downloadLink,
                    accountId = requireNotNull(application.account.id),
                    subscription = application.adminRoleId
                )
                val token = account.value?.token ?: throw Exception("Token is null")
                applicationRepository.updateApplicationById(
                    token, application.id, body
                )
                val apps = _applications.value.toMutableList()
                val index = apps.indexOfFirst { it.id == application.id }
                apps[index] = application
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                applicationUiState = ApplicationUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun deleteApplicationById(application: Application) {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                applicationRepository.deleteApplicationById(token, application.id)
                val apps = _applications.value.toMutableList()
                apps.remove(application)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                applicationUiState = ApplicationUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun deleteSelectedApplicationEntry() {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    applicationRepository.deleteSelectedApplicationEntry()
                }
                _application.value = null
                applicationUiState = ApplicationUiState.Success(_applications.value)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun createApplication(name: String) {
        viewModelScope.launch {
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val accountId = account.value?.account?.id ?: throw Exception("Account id is null")
                val body = CreateApplicationBody(
                    name = name,
                    accountId = accountId
                )
                val application = applicationRepository.createApplication(token, body)
                val apps = _applications.value.toMutableList()
                apps.add(application)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                applicationUiState = ApplicationUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                Log.d("ApplicationViewModel", e.stackTraceToString())
                applicationUiState = ApplicationUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [ApplicationViewModel] that takes [ApplicationRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BlitzWareApplication)
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                ApplicationViewModel(
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}