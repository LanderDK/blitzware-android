package com.example.blitzware_android.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blitzware_android.data.ApplicationRepository
import com.example.blitzware_android.data.DefaultAppContainer
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateApplicationBody
import com.example.blitzware_android.model.UpdateApplicationBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ApplicationUiState {
    data class Success(val apps: List<Application>) : ApplicationUiState
    object Error : ApplicationUiState
    object Loading : ApplicationUiState
}

class ApplicationViewModel(private val accountViewModel: AccountViewModel) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var applicationUiState: ApplicationUiState by mutableStateOf(ApplicationUiState.Loading)
        private set

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> get() = _applications


    private val _application = mutableStateOf<Application?>(null)
    val application: Application?
        get() = _application.value

    init {
        getApplicationsOfAccount()
    }

    private fun getApplicationsOfAccount() {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val accountId = accountViewModel.account?.account?.id ?: throw Exception("Account is null")
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val apps = DefaultAppContainer().applicationRepository.getApplicationsOfAccount(token, accountId)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: HttpException) {
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            }
        }
    }

    fun getApplicationById(application: Application) {
        viewModelScope.launch {
            applicationUiState = ApplicationUiState.Loading
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val app = DefaultAppContainer().applicationRepository.getApplicationById(token, application.id)
                _application.value = app
                applicationUiState = ApplicationUiState.Success(_applications.value)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: HttpException) {
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            }
        }
    }

    fun updateApplicationById(application: Application) {
        viewModelScope.launch {
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
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                DefaultAppContainer().applicationRepository.updateApplicationById(
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
                applicationUiState = ApplicationUiState.Error
            } catch (e: HttpException) {
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            }
        }
    }

    fun deleteApplicationById(application: Application) {
        viewModelScope.launch {
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                DefaultAppContainer().applicationRepository.deleteApplicationById(token, application.id)
                val apps = _applications.value.toMutableList()
                apps.remove(application)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: HttpException) {
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            }
        }
    }

    fun createApplication(name: String) {
        viewModelScope.launch {
            try {
                val token = accountViewModel.account?.token ?: throw Exception("Token is null")
                val accountId = accountViewModel.account?.account?.id ?: throw Exception("Account is null")
                val body = CreateApplicationBody(
                    name = name,
                    accountId = accountId
                )
                val application = DefaultAppContainer().applicationRepository.createApplication(token, body)
                val apps = _applications.value.toMutableList()
                apps.add(application)
                _applications.value = apps
                applicationUiState = ApplicationUiState.Success(apps)
            } catch (e: IOException) {
                Log.d("ApplicationViewModel", "IOException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: HttpException) {
                Log.d("ApplicationViewModel", "HttpException")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            } catch (e: Exception) {
                Log.d("ApplicationViewModel", "Exception")
                Log.d("ApplicationViewModel", e.message.toString())
                applicationUiState = ApplicationUiState.Error
            }
        }
    }

    /**
     * Factory for [ApplicationViewModel] that takes [ApplicationRepository] and [AccountViewModel] as a dependency
     */
//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as BlitzWareApplication)
//                val applicationRepository = application.container.applicationRepository
//                ApplicationViewModel(applicationRepository = applicationRepository)
//            }
//        }
//    }
}
