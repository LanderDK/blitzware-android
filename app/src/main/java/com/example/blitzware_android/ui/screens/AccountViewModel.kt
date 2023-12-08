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
import com.example.blitzware_android.data.AccountRepository
import com.example.blitzware_android.model.Account
import kotlinx.coroutines.launch
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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            accountUiState = AccountUiState.Loading
            accountUiState = try {
                val body = mapOf(
                    "username" to username,
                    "password" to password
                )
                AccountUiState.Success(accountRepository.login(body))
            } catch (e: IOException) {
                Log.d("ViewModel", "IOException")
                Log.d("ViewModel", e.message.toString())
                AccountUiState.Error
            } catch (e: HttpException) {
                Log.d("ViewModel", "HttpException")
                Log.d("ViewModel", e.message.toString())
                AccountUiState.Error
            }
            Log.d("ViewModel", accountUiState.toString())
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
