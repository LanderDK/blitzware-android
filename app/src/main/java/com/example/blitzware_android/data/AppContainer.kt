package com.example.blitzware_android.data

import com.example.blitzware_android.network.AccountApiService
import com.example.blitzware_android.network.AppLogApiService
import com.example.blitzware_android.network.ApplicationApiService
import com.example.blitzware_android.network.ChatMessageApiService
import com.example.blitzware_android.network.FileApiService
import com.example.blitzware_android.network.LicenseApiService
import com.example.blitzware_android.network.LogApiService
import com.example.blitzware_android.network.UserApiService
import com.example.blitzware_android.network.UserSubApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val accountRepository: AccountRepository
    val applicationRepository: ApplicationRepository
    val chatMessageRepository: ChatMessageRepository
    val logRepository: LogRepository
    val userRepository: UserRepository
    val licenseRepository: LicenseRepository
    val userSubRepository: UserSubRepository
    val fileRepository: FileRepository
    val appLogRepository: AppLogRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.0.227:9000/api/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: AccountApiService by lazy {
        retrofit.create(AccountApiService::class.java)
    }

    /**
     * DI implementation for Account repository
     */
    override val accountRepository: AccountRepository by lazy {
        NetworkAccountRepository(retrofit.create(AccountApiService::class.java))
    }

    /**
     * DI implementation for Application repository
     */
    override val applicationRepository: ApplicationRepository by lazy {
        NetworkApplicationRepository(retrofit.create(ApplicationApiService::class.java))
    }

    /**
     * DI implementation for ChatMessage repository
     */
    override val chatMessageRepository: ChatMessageRepository by lazy {
        NetworkChatMessageRepository(retrofit.create(ChatMessageApiService::class.java))
    }

    /**
     * DI implementation for Log repository
     */
    override val logRepository: LogRepository by lazy {
        NetworkLogRepository(retrofit.create(LogApiService::class.java))
    }

    /**
     * DI implementation for User repository
     */
    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(retrofit.create(UserApiService::class.java))
    }

    /**
     * DI implementation for License repository
     */
    override val licenseRepository: LicenseRepository by lazy {
        NetworkLicenseRepository(retrofit.create(LicenseApiService::class.java))
    }

    /**
     * DI implementation for UserSub repository
     */
    override val userSubRepository: UserSubRepository by lazy {
        NetworkUserSubRepository(retrofit.create(UserSubApiService::class.java))
    }

    /**
     * DI implementation for File repository
     */
    override val fileRepository: FileRepository by lazy {
        NetworkFileRepository(retrofit.create(FileApiService::class.java))
    }

    /**
     * DI implementation for AppLog repository
     */
    override val appLogRepository: AppLogRepository by lazy {
        NetworkAppLogRepository(retrofit.create(AppLogApiService::class.java))
    }
}
