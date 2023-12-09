package com.example.blitzware_android.ui.screens

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
import com.example.blitzware_android.data.ApplicationRepository
import com.example.blitzware_android.data.DefaultAppContainer
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.network.ApplicationApiService
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
            }
            Log.d("ApplicationViewModel", applicationUiState.toString())
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
