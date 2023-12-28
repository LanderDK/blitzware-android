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
import com.example.blitzware_android.data.LicenseRepository
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.model.CreateLicenseBody
import com.example.blitzware_android.model.License
import com.example.blitzware_android.model.UpdateLicenseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * License ui state
 *
 * @constructor Create empty License ui state
 */
sealed interface LicenseUiState {
    /**
     * Success
     *
     * @property licenses
     * @constructor Create empty Success
     */
    data class Success(val licenses: List<License>) : LicenseUiState

    /**
     * Error
     *
     * @property code
     * @property message
     * @constructor Create empty Error
     */
    data class Error(val code: String, val message: String) : LicenseUiState
    object Loading : LicenseUiState
}

/**
 * License view model
 *
 * @property licenseRepository
 * @property applicationRepository
 * @property accountRepository
 * @constructor Create empty License view model
 */
class LicenseViewModel(
    private val licenseRepository: LicenseRepository,
    private val applicationRepository: ApplicationRepository,
    private val accountRepository: AccountRepository
): ViewModel() {
    var licenseUiState: LicenseUiState by mutableStateOf(LicenseUiState.Loading)

    private val _licenses = MutableStateFlow<List<License>>(emptyList())
    val licenses: StateFlow<List<License>> get() = _licenses

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
            getLicensesOfApplication()
        }
    }

    private fun getLicensesOfApplication() {
        viewModelScope.launch {
            licenseUiState = LicenseUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId =
                    application.value?.id ?: throw Exception("Application id is null")
                val licenses = licenseRepository.getLicensesOfApplication(token, applicationId)
                _licenses.value = licenses
                licenseUiState = LicenseUiState.Success(licenses)
            } catch (e: IOException) {
                Log.d("LicenseViewModel", "IOException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LicenseViewModel", "HttpException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                licenseUiState = LicenseUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("LicenseViewModel", "Exception")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Create license
     *
     * @param days
     * @param format
     * @param amount
     * @param subscription
     */
    fun createLicense(
        days: Int,
        format: String,
        amount: Int,
        subscription: Int
    ) {
        viewModelScope.launch {
            licenseUiState = LicenseUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val applicationId = application.value?.id ?: throw Exception("Application id is null")
                val body = CreateLicenseBody(
                    days = days,
                    format = format,
                    amount = amount,
                    subscription = subscription,
                    applicationId = applicationId
                )
                val newLicenses = licenseRepository.createLicense(token, body)
                val licenses = _licenses.value.toMutableList()
                licenses.addAll(newLicenses)
                _licenses.value = licenses
                licenseUiState = LicenseUiState.Success(licenses)
            } catch (e: IOException) {
                Log.d("LicenseViewModel", "IOException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LicenseViewModel", "HttpException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                licenseUiState = LicenseUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("LicenseViewModel", "Exception")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Update license by id
     *
     * @param id
     * @param license
     * @param days
     * @param used
     * @param enabled
     * @param subscription
     */
    fun updateLicenseById(
        id: String,
        license: String,
        days: Int,
        used: Int,
        enabled: Int,
        subscription: Int,
    ) {
        viewModelScope.launch {
            licenseUiState = LicenseUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val body = UpdateLicenseBody(
                    license = license,
                    days = days,
                    used = used,
                    enabled = enabled,
                    subscription = subscription
                )
                licenseRepository.updateLicenseById(token, id, body)
                val licenses = _licenses.value.toMutableList()
                val updatedLicense = licenses.find { it.id == id } ?: throw Exception("License not found")
                updatedLicense.license = license
                updatedLicense.used = used
                updatedLicense.days = days
                updatedLicense.enabled = enabled
                _licenses.value = licenses
                licenseUiState = LicenseUiState.Success(licenses)
            } catch (e: IOException) {
                Log.d("LicenseViewModel", "IOException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LicenseViewModel", "HttpException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                licenseUiState = LicenseUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("LicenseViewModel", "Exception")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Update license by id2
     *
     * @param license
     */
    fun updateLicenseById2(license: License) {
        viewModelScope.launch {
            licenseUiState = LicenseUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                val body = UpdateLicenseBody(
                    license = license.license,
                    days = license.days,
                    used = license.used,
                    enabled = license.enabled,
                    subscription = license.userSubId
                )
                licenseRepository.updateLicenseById(token, license.id, body)
                val licenses = _licenses.value.toMutableList()
                val index = licenses.indexOfFirst { it.id == license.id }
                licenses[index] = license
                _licenses.value = licenses
                licenseUiState = LicenseUiState.Success(licenses)
            } catch (e: IOException) {
                Log.d("LicenseViewModel", "IOException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LicenseViewModel", "HttpException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                licenseUiState = LicenseUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("LicenseViewModel", "Exception")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Delete license by id
     *
     * @param license
     */
    fun deleteLicenseById(license: License) {
        viewModelScope.launch {
            licenseUiState = LicenseUiState.Loading
            try {
                val token = account.value?.token ?: throw Exception("Token is null")
                licenseRepository.deleteLicenseById(token, license.id)
                val licenses = _licenses.value.toMutableList()
                licenses.remove(license)
                _licenses.value = licenses
                licenseUiState = LicenseUiState.Success(licenses)
            } catch (e: IOException) {
                Log.d("LicenseViewModel", "IOException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("IOException", e.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LicenseViewModel", "HttpException")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", "Error response: $errorBody")
                val jsonObject = JSONObject(errorBody!!)
                val code = jsonObject.getString("code")
                val message = jsonObject.getString("message")
                licenseUiState = LicenseUiState.Error(code, message)
            } catch (e: Exception) {
                Log.d("LicenseViewModel", "Exception")
                Log.d("LicenseViewModel", e.message.toString())
                Log.d("LicenseViewModel", e.stackTraceToString())
                licenseUiState = LicenseUiState.Error("Exception", e.message.toString())
            }
        }
    }

    /**
     * Factory for [LicenseViewModel] that takes [LicenseRepository], [ApplicationRepository] and [AccountRepository] as a dependencies
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BlitzWareApplication)
                val licenseRepository = application.container.licenseRepository
                val applicationRepository = application.container.applicationRepository
                val accountRepository = application.container.accountRepository
                LicenseViewModel(
                    licenseRepository = licenseRepository,
                    applicationRepository = applicationRepository,
                    accountRepository = accountRepository
                )
            }
        }
    }
}