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
import com.example.blitzware_android.model.UserSubBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

sealed interface UserSubUiState {
    data class Success(val userSubs: List<UserSub>) : UserSubUiState
    data class Error(val code: String, val message: String) : UserSubUiState
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
                userSubUiState = UserSubUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userSubUiState = UserSubUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("Exception", e.message.toString())
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
                val body = UserSubBody(name, level, applicationId)
                val userSub = userSubRepository.createUserSub(token, body)
                val userSubs = _userSubs.value.toMutableList()
                userSubs.add(userSub)
                _userSubs.value = userSubs
                userSubUiState = UserSubUiState.Success(userSubs)
            } catch (e: IOException) {
                Log.d("UserSubViewModel", "IOException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userSubUiState = UserSubUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun updateUserSubById(id: Int, name: String, level: Int) {
        viewModelScope.launch {
            userSubUiState = UserSubUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val appId = application.value?.id ?: throw Exception("Application id is null")
                val body = UserSubBody(name, level, appId)
                userSubRepository.updateUserSubById(token, id, body)
                val userSubs = _userSubs.value.toMutableList()
                val userSub = userSubs.find { it.id == id } ?: throw Exception("UserSub not found")
                userSub.name = name
                userSub.level = level
                _userSubs.value = userSubs
                userSubUiState = UserSubUiState.Success(userSubs)
            } catch (e: IOException) {
                Log.d("UserSubViewModel", "IOException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userSubUiState = UserSubUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("Exception", e.message.toString())
            }
        }
    }

    fun deleteUserSubById(userSub: UserSub) {
        viewModelScope.launch {
            userSubUiState = UserSubUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                userSubRepository.deleteUserSubById(token, userSub.id)
                val userSubs = _userSubs.value.toMutableList()
                userSubs.remove(userSub)
                _userSubs.value = userSubs
                userSubUiState = UserSubUiState.Success(userSubs)
            } catch (e: IOException) {
                Log.d("UserSubViewModel", "IOException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserSubViewModel", "HttpException")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userSubUiState = UserSubUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserSubViewModel", "Exception")
                Log.d("UserSubViewModel", e.message.toString())
                Log.d("UserSubViewModel", e.stackTraceToString())
                userSubUiState = UserSubUiState.Error("Exception", e.message.toString())
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