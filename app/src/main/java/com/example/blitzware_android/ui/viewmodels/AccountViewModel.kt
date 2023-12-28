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
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.UpdateAccountPicBody
import com.example.blitzware_android.model.toFormattedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * Account ui state
 *
 * @constructor Create empty Account ui state
 */
sealed interface AccountUiState {
    /**
     * Success
     *
     * @property account
     * @constructor Create empty Success
     */
    data class Success(val account: Account) : AccountUiState

    /**
     * Error
     *
     * @property code
     * @property message
     * @constructor Create empty Error
     */
    data class Error(val code: String, val message: String) : AccountUiState
    object Loading : AccountUiState
}

/**
 * Account view model
 *
 * @property accountRepository
 * @constructor Create empty Account view model
 */
class AccountViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var accountUiState: AccountUiState by mutableStateOf(AccountUiState.Loading)
        private set

    private val _account = mutableStateOf<Account?>(null)
    val account: Account?
        get() = _account.value

    private val _isAuthed = mutableStateOf(false)

    val registerScreen = mutableStateOf(false)

    val isAuthed: Boolean
        get() = _isAuthed.value

    private val _twoFactorRequired = mutableStateOf(false)
    val twoFactorRequired: Boolean
        get() = _twoFactorRequired.value

    private val _otpRequired = mutableStateOf(false)
    val otpRequired: Boolean
        get() = _otpRequired.value

    /**
     * Login
     *
     * @param username
     * @param password
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
             try {
                 withContext(Dispatchers.IO) {
                        accountRepository.deleteAccountEntry()
                 }
                val body = mapOf(
                    "username" to username,
                    "password" to password
                )
                 val account = accountRepository.login(body)
                 _account.value = account
                 accountUiState = AccountUiState.Success(account)
                 accountRepository.insertAccount(account)
                 _isAuthed.value = true
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                 Log.d("AccountViewModel", e.stackTraceToString())
                 accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                 val errorBody = e.response()?.errorBody()?.string()
                 Log.d("AccountViewModel", "HttpException")
                 Log.d("AccountViewModel", e.message.toString())
                 Log.d("AccountViewModel", "Error response: $errorBody")
                 val jsonObject = JSONObject(errorBody!!)
                 val code = jsonObject.getString("code")
                 val message = jsonObject.getString("message")
                 if (message == "2FA required") {
                     _twoFactorRequired.value = true
                 } else if (message == "We need to verify it is you, check your email") {
                     _otpRequired.value = true
                 }
                 accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                 Log.d("AccountViewModel", "Exception")
                 Log.d("AccountViewModel", e.message.toString())
                 Log.d("AccountViewModel", e.stackTraceToString())
                 accountUiState = AccountUiState.Error("Exception", e.message.toString())
             }
        }
    }

    /**
     * Register
     *
     * @param username
     * @param email
     * @param password
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                val body = mapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password,
                    "recaptchaValue" to "N/A"
                )
                accountRepository.register(body)
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Logout
     *
     */
    fun logout() {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    _account.value = accountRepository.getAccountStream()
                }
                val token = account?.token ?: throw Exception("Token is null")
                accountRepository.logout(token)
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Get account by id
     *
     */
    fun getAccountById() {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    _account.value = accountRepository.getAccountStream()
                    val accountId = account?.account?.id ?: throw Exception("Account is null")
                    val token = account?.token ?: throw Exception("Token is null")
                    val account = accountRepository.getAccountById(token, accountId)
                    _account.value?.account = account
                    accountRepository.updateAccount(_account.value!!)
                    accountUiState = AccountUiState.Success(_account.value!!)
                }
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Update account profile picture by id
     *
     * @param body
     */
    fun updateAccountProfilePictureById(body: UpdateAccountPicBody) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    _account.value = accountRepository.getAccountStream()
                    val accountId = account?.account?.id ?: throw Exception("Account is null")
                    val token = account?.token ?: throw Exception("Token is null")
                    accountRepository.updateAccountProfilePictureById(token, accountId, body)
                    _account.value?.account?.profilePicture = body.toFormattedString()
                    accountRepository.updateAccount(_account.value!!)
                    accountUiState = AccountUiState.Success(_account.value!!)
                }
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Verify login o t p
     *
     * @param username
     * @param otp
     */
    fun verifyLoginOTP(username: String, otp: String) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    accountRepository.deleteAccountEntry()
                }
                val body = mapOf(
                    "username" to username,
                    "otp" to otp
                )
                val account = accountRepository.verifyLoginOTP(body)
                _otpRequired.value = false
                _account.value = account
                accountUiState = AccountUiState.Success(account)
                accountRepository.insertAccount(account)
                _isAuthed.value = true
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Verify login2f a
     *
     * @param username
     * @param twoFactorCode
     */
    fun verifyLogin2FA(username: String, twoFactorCode: String) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            try {
                withContext(Dispatchers.IO) {
                    accountRepository.deleteAccountEntry()
                }
                val body = mapOf(
                    "username" to username,
                    "twoFactorCode" to twoFactorCode
                )
                val account = accountRepository.verifyLogin2FA(body)
                _twoFactorRequired.value = false
                _account.value = account
                accountUiState = AccountUiState.Success(account)
                accountRepository.insertAccount(account)
                _isAuthed.value = true
            } catch (e: IOException) {
                Log.d("AccountViewModel", "IOException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                if (message == "We need to verify it is you, check your email") {
                    _twoFactorRequired.value = false
                    _otpRequired.value = true
                }
                accountUiState = AccountUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                Log.d("AccountViewModel", e.stackTraceToString())
                accountUiState = AccountUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [AccountViewModel] that takes [AccountRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BlitzWareApplication)
                val accountRepository = application.container.accountRepository
                AccountViewModel(accountRepository = accountRepository)
            }
        }
    }
}
