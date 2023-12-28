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
import com.example.blitzware_android.data.FileRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * File ui state
 *
 * @constructor Create empty File ui state
 */
sealed interface FileUiState {
    /**
     * Success
     *
     * @property files
     * @constructor Create empty Success
     */
    data class Success(val files: List<File>) : FileUiState

    /**
     * Error
     *
     * @property code
     * @property message
     * @constructor Create empty Error
     */
    data class Error(val code: String, val message: String) : FileUiState
    object Loading : FileUiState
}

/**
 * File view model
 *
 * @property fileRepository
 * @property applicationRepository
 * @property accountRepository
 * @constructor Create empty File view model
 */
class FileViewModel(
    private val fileRepository: FileRepository,
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    var fileUiState: FileUiState by mutableStateOf(FileUiState.Loading)

    private val _files = MutableStateFlow<List<File>>(emptyList())
    val files: StateFlow<List<File>> get() = _files

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
            getFilesOfApplication()
        }
    }

    private fun getFilesOfApplication() {
        viewModelScope.launch {
            fileUiState = FileUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId =
                    application.value?.id ?: throw Exception("Application id is null")
                val files = fileRepository.getFilesOfApplication(token, applicationId)
                _files.value = files
                fileUiState = FileUiState.Success(files)
            } catch (e: IOException) {
                Log.d("FileViewModel", "IOException")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", e.stackTraceToString())
                fileUiState = FileUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("FileViewModel", "HttpException")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                fileUiState = FileUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("FileViewModel", "Exception")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", e.stackTraceToString())
                fileUiState = FileUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Delete file by id
     *
     * @param file
     */
    fun deleteFileById(file: File) {
        viewModelScope.launch {
            fileUiState = FileUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                fileRepository.deleteFileById(token, file.id)
                val files = _files.value.toMutableList()
                files.remove(file)
                _files.value = files
                fileUiState = FileUiState.Success(files)
            } catch (e: IOException) {
                Log.d("FileViewModel", "IOException")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", e.stackTraceToString())
                fileUiState = FileUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("FileViewModel", "HttpException")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                fileUiState = FileUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("FileViewModel", "Exception")
                Log.d("FileViewModel", e.message.toString())
                Log.d("FileViewModel", e.stackTraceToString())
                fileUiState = FileUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [FileViewModel] that takes [FileRepository], [ApplicationRepository] and [AccountRepository] as a dependencies
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val fileRepository = application.container.fileRepository
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                FileViewModel(
                    fileRepository = fileRepository,
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}