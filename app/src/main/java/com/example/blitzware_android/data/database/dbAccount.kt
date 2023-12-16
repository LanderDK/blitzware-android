package com.example.blitzware_android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData

/**
 * Entity data class represents a single row in the database.
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