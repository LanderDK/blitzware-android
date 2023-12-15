package com.example.blitzware_android.data

import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData
import com.example.blitzware_android.model.UpdateAccountPicBody
import com.example.blitzware_android.network.AccountApiService

interface AccountRepository {
    suspend fun login(body: Map<String, String>): Account

    suspend fun getAccountById(token: String, id: String): AccountData

    suspend fun updateAccountProfilePictureById(
        token: String,
        id: String,
        body: UpdateAccountPicBody
    )
}

class NetworkAccountRepository(
    private val accountApiService: AccountApiService
) : AccountRepository {
    override suspend fun login(body: Map<String, String>): Account {
        return accountApiService.login(body = body)
    }

    override suspend fun getAccountById(token: String, id: String): AccountData {
        val authorizationHeader = "Bearer $token"
        return accountApiService.getAccountById(authorizationHeader, id)
    }

    override suspend fun updateAccountProfilePictureById(
        token: String,
        id: String,
        body: UpdateAccountPicBody
    ) {
        val authorizationHeader = "Bearer $token"
        accountApiService.updateAccountProfilePictureById(authorizationHeader, id, body)
    }
}
