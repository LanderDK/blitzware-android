package com.example.blitzware_android.data

import com.example.blitzware_android.network.AccountApiService
import com.example.blitzware_android.network.ApplicationApiService
import com.example.blitzware_android.network.ChatMessageApiService
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
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.1.62:9000/api/"

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
}
