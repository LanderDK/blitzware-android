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
import com.example.blitzware_android.data.UserRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateUserBody
import com.example.blitzware_android.model.UpdateUserBody
import com.example.blitzware_android.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime

/**
 * User ui state
 *
 * @constructor Create empty User ui state
 */
sealed interface UserUiState {
    /**
     * Success
     *
     * @property users
     * @constructor Create empty Success
     */
    data class Success(val users: List<User>) : UserUiState

    /**
     * Error
     *
     * @property code
     * @property message
     * @constructor Create empty Error
     */
    data class Error(val code: String, val message: String) : UserUiState
    object Loading : UserUiState
}

/**
 * User view model
 *
 * @property userRepository
 * @property applicationRepository
 * @property accountRepository
 * @constructor Create empty User view model
 */
class UserViewModel(
    private val userRepository: UserRepository,
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    var userUiState: UserUiState by mutableStateOf(UserUiState.Loading)

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

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
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
            getUsersOfApplication()
        }
    }

    private fun getUsersOfApplication() {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId =
                    application.value?.id ?: throw Exception("Application id is null")
                val users = userRepository.getUsersOfApplication(token, applicationId)
                _users.value = users
                userUiState = UserUiState.Success(users)
            } catch (e: IOException) {
                Log.d("UserViewModel", "IOException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserViewModel", "HttpException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userUiState = UserUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Create user from dashboard
     *
     * @param name
     * @param email
     * @param password
     * @param subscription
     */
    fun createUserFromDashboard(
        name: String,
        email: String,
        password: String,
        subscription: Int
    ) {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId = application.value?.id ?: throw Exception("Application id is null")
                val expiryDate = LocalDateTime.now().plusDays(1)
                val expiryDateString = expiryDate.toString()
                val body = CreateUserBody(
                    username = name,
                    email = email,
                    password = password,
                    expiry = expiryDateString,
                    subscription = subscription,
                    id = applicationId
                )
                val user = userRepository.createUserFromDashboard(token, body)
                val users = _users.value.toMutableList()
                users.add(user)
                _users.value = users
                userUiState = UserUiState.Success(users)
            } catch (e: IOException) {
                Log.d("UserViewModel", "IOException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserViewModel", "HttpException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userUiState = UserUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Update user by id
     *
     * @param id
     * @param username
     * @param email
     * @param expiryDate
     * @param hwid
     * @param twoFactorAuth
     * @param enabled
     * @param subscription
     */
    fun updateUserById(
        id: String,
        username: String,
        email: String,
        expiryDate: String,
        hwid: String,
        twoFactorAuth: Int,
        enabled: Int,
        subscription: Int
    ) {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val body = UpdateUserBody(
                    username = username,
                    email = email,
                    expiryDate = expiryDate,
                    hwid = hwid,
                    twoFactorAuth = twoFactorAuth,
                    enabled = enabled,
                    subscription = subscription
                )
                userRepository.updateUserById(token, id, body)
                val users = _users.value.toMutableList()
                val user = users.find { it.id == id } ?: throw Exception("User not found")
                user.username = username
                user.email = email
                user.hwid = hwid
                user.twoFactorAuth = twoFactorAuth
                user.enabled = enabled
                _users.value = users
                userUiState = UserUiState.Success(users)
            } catch (e: IOException) {
                Log.d("UserViewModel", "IOException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserViewModel", "HttpException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userUiState = UserUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Update user by id2
     *
     * @param user
     */
    fun updateUserById2(user: User) {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val body = UpdateUserBody(
                    username = user.username,
                    email = user.email,
                    expiryDate = user.expiryDate,
                    hwid = user.hwid,
                    twoFactorAuth = user.twoFactorAuth,
                    enabled = user.enabled,
                    subscription = user.userSubId
                )
                userRepository.updateUserById(token, user.id, body)
                val users = _users.value.toMutableList()
                val index = users.indexOfFirst { it.id == user.id }
                users[index] = user
                _users.value = users
                userUiState = UserUiState.Success(users)
            } catch (e: IOException) {
                Log.d("UserViewModel", "IOException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserViewModel", "HttpException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userUiState = UserUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Delete user by id
     *
     * @param user
     */
    fun deleteUserById(user: User) {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                userRepository.deleteUserById(token, user.id)
                val users = _users.value.toMutableList()
                users.remove(user)
                _users.value = users
                userUiState = UserUiState.Success(users)
            } catch (e: IOException) {
                Log.d("UserViewModel", "IOException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("UserViewModel", "HttpException")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                userUiState = UserUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("UserViewModel", "Exception")
                Log.d("UserViewModel", e.message.toString())
                Log.d("UserViewModel", e.stackTraceToString())
                userUiState = UserUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [UserViewModel] that takes [UserRepository], [ApplicationRepository] and [AccountRepository] as a dependencies
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val userRepository = application.container.userRepository
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                UserViewModel(
                    userRepository = userRepository,
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}