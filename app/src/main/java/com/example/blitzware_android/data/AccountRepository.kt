package com.example.blitzware_android.data

import com.example.blitzware_android.data.database.AccountDao
import com.example.blitzware_android.data.database.asDbAccount
import com.example.blitzware_android.data.database.asDomainAccount
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData
import com.example.blitzware_android.model.UpdateAccountPicBody
import com.example.blitzware_android.network.AccountApiService

/**
 * Account repository
 *
 * @constructor Create empty Account repository
 */
interface AccountRepository {
    /**
     * Login
     *
     * @param body
     * @return
     */
    suspend fun login(body: Map<String, String>): Account

    /**
     * Register
     *
     * @param body
     */
    suspend fun register(body: Map<String, String>)

    /**
     * Logout
     *
     * @param token
     */
    suspend fun logout(token: String)

    /**
     * Get account by id
     *
     * @param token
     * @param id
     * @return
     */
    suspend fun getAccountById(token: String, id: String): AccountData

    /**
     * Update account profile picture by id
     *
     * @param token
     * @param id
     * @param body
     */
    suspend fun updateAccountProfilePictureById(
        token: String,
        id: String,
        body: UpdateAccountPicBody
    )

    /**
     * Verify login o t p
     *
     * @param body
     * @return
     */
    suspend fun verifyLoginOTP(body: Map<String, String>): Account

    /**
     * Verify login2f a
     *
     * @param body
     * @return
     */
    suspend fun verifyLogin2FA(body: Map<String, String>): Account

    /**
     * Get account stream
     *
     * @return
     */
    fun getAccountStream(): Account

    /**
     * Insert account
     *
     * @param account
     */
    suspend fun insertAccount(account: Account)

    /**
     * Delete account
     *
     * @param account
     */
    suspend fun deleteAccount(account: Account)

    /**
     * Delete account entry
     *
     */
    suspend fun deleteAccountEntry()

    /**
     * Update account
     *
     * @param account
     */
    suspend fun updateAccount(account: Account)
}

/**
 * Network account repository
 *
 * @property accountDao
 * @property accountApiService
 * @constructor Create empty Network account repository
 */
class NetworkAccountRepository(
    private val accountDao: AccountDao,
    private val accountApiService: AccountApiService
) : AccountRepository {
    override suspend fun login(body: Map<String, String>): Account {
        return accountApiService.login(body)
    }

    override suspend fun register(body: Map<String, String>) {
        accountApiService.register(body)
    }

    override suspend fun logout(token: String) {
        val authorizationHeader = "Bearer $token"
        accountApiService.logout(authorizationHeader)
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