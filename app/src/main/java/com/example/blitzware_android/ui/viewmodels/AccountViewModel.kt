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
import retrofit2.HttpException
import java.io.IOException

sealed interface AccountUiState {
    data class Success(val account: Account) : AccountUiState
    object Error : AccountUiState
    object Loading : AccountUiState
}

class AccountViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var accountUiState: AccountUiState by mutableStateOf(AccountUiState.Loading)
        private set

    private val _account = mutableStateOf<Account?>(null)
    val account: Account?
        get() = _account.value

    private val _isAuthed = mutableStateOf(false)
    val isAuthed: Boolean
        get() = _isAuthed.value

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
                 accountUiState = AccountUiState.Error
            } catch (e: HttpException) {
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                 accountUiState = AccountUiState.Error
            } catch (e: Exception) {
                 Log.d("AccountViewModel", "Exception")
                 Log.d("AccountViewModel", e.message.toString())
                 accountUiState = AccountUiState.Error
             }
        }
    }

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
                accountUiState = AccountUiState.Error
            } catch (e: HttpException) {
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                accountUiState = AccountUiState.Error
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                accountUiState = AccountUiState.Error
            }
        }
    }

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
                accountUiState = AccountUiState.Error
            } catch (e: HttpException) {
                Log.d("AccountViewModel", "HttpException")
                Log.d("AccountViewModel", e.message.toString())
                accountUiState = AccountUiState.Error
            } catch (e: Exception) {
                Log.d("AccountViewModel", "Exception")
                Log.d("AccountViewModel", e.message.toString())
                accountUiState = AccountUiState.Error
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
