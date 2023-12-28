package com.example.blitzware_android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData

/**
 * Db account
 *
 * @property id
 * @property username
 * @property email
 * @property role
 * @property creationDate
 * @property profilePicture
 * @property emailVerified
 * @property twoFactorAuth
 * @property enabled
 * @property token
 * @constructor Create empty Db account
 */

@Entity(tableName = "accounts")
data class dbAccount(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    val creationDate: String,
    val profilePicture: String?,
    val emailVerified: Int,
    val twoFactorAuth: Int,
    val enabled: Int,
    val token: String
)

/**
 * As domain account
 *
 * @return
 */
fun dbAccount.asDomainAccount(): Account {
    return Account(
        account = AccountData(
            id = this.id,
            username = this.username,
            email = this.email,
            roles = this.role.split(","),
            creationDate = this.creationDate,
            profilePicture = this.profilePicture,
            emailVerified = this.emailVerified,
            twoFactorAuth = this.twoFactorAuth,
            enabled = this.enabled
        ),
        token = this.token
    )
}

/**
 * As db account
 *
 * @return
 */
fun Account.asDbAccount(): dbAccount {
    return dbAccount(
        id = this.account.id,
        username = this.account.username,
        email = this.account.email,
        role = this.account.roles[0],
        creationDate = this.account.creationDate,
        profilePicture = this.account.profilePicture,
        emailVerified = this.account.emailVerified,
        twoFactorAuth = this.account.twoFactorAuth,
        enabled = this.account.enabled,
        token = this.token
    )
}