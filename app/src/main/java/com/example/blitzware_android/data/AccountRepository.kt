package com.example.blitzware_android.data

import com.example.blitzware_android.data.database.AccountDao
import com.example.blitzware_android.data.database.asDbAccount
import com.example.blitzware_android.data.database.asDomainAccount
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

    suspend fun verifyLoginOTP(body: Map<String, String>): Account

    suspend fun verifyLogin2FA(body: Map<String, String>): Account

    /**
     * Retrieve an account (only one will exist) from the given data source.
     */
    fun getAccountStream(): Account

    /**
     * Insert account in the data source
     */
    suspend fun insertAccount(account: Account)

    /**
     * Delete account from the data source
     */
    suspend fun deleteAccount(account: Account)

    /**
     * Delete all accounts from the data source
     */
    suspend fun deleteAccountEntry()

    /**
     * Update account in the data source
     */
    suspend fun updateAccount(account: Account)
}

class NetworkAccountRepository(
    private val accountDao: AccountDao,
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

    override suspend fun verifyLoginOTP(body: Map<String, String>): Account {
        return accountApiService.verifyLoginOTP(body)
    }

    override suspend fun verifyLogin2FA(body: Map<String, String>): Account {
        return accountApiService.verifyLogin2FA(body)
    }

    override fun getAccountStream(): Account {
        return accountDao.getAccount().asDomainAccount()
    }

    override suspend fun insertAccount(account: Account) {
        accountDao.insert(account.asDbAccount())
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.delete(account.asDbAccount())
    }

    override suspend fun deleteAccountEntry() {
        accountDao.deleteAll()
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.update(account.asDbAccount())
    }
}