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
import com.example.blitzware_android.data.ApplicationRepository
import com.example.blitzware_android.data.UserSubRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.UserSub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed interface UserSubUiState {
    data class Success(val userSubs: List<UserSub>) : UserSubUiState
    object Error : UserSubUiState
    object Loading : UserSubUiState
}

class UserSubViewModel(
    private val userSubRepository: UserSubRepository,
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    var userSubUiState: UserSubUiState by mutableStateOf(UserSubUiState.Loading)

    private val _userSubs = MutableStateFlow<List<UserSub>>(emptyList())
    val userSubs: StateFlow<List<UserSub>> get() = _userSubs

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
            _application.value = application.asApplication(_account.value!!)
            getUserSubsOfApplication()
        }
    }

    private fun getUserSubsOfApplication() {
        viewModelScope.launch {
            userSubUiState = UserSubUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId =
                    application.value?.id ?: throw Exception("Application id is null")
                val userSubs = userSubRepository.getUserSubsOfApplication(token, applicationId)
                _userSubs.value = userSubs
                userSubUiState = UserSubUiState.Success(userSubs)
            } catch (e: IOException) {
                Log.d("UserSubViewModel", "IOException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            } catch (e: HttpException) {
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            }
        }
    }

    fun createUserSub(
        name: String,
        level: Int
    ) {
        viewModelScope.launch {
            userSubUiState = UserSubUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId = application.value?.id ?: throw Exception("Application id is null")

            } catch (e: IOException) {
                Log.d("UserSubViewModel", "IOException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            } catch (e: HttpException) {
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error
            }
        }
    }

    /**
     * Factory for [UserSubViewModel] that takes [UserSubRepository], [ApplicationRepository] and [AccountRepository] as a dependencies
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val userSubRepository = application.container.userSubRepository
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                UserSubViewModel(
                    userSubRepository = userSubRepository,
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}