package com.example.blitzware_android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountOfApp
import com.example.blitzware_android.model.Application

/**
 * Db selected application
 *
 * @property id
 * @property name
 * @property secret
 * @property version
 * @property status
 * @property developerMode
 * @property twoFactorAuth
 * @property hwidCheck
 * @property freeMode
 * @property integrityCheck
 * @property programHash
 * @property downloadLink
 * @property adminRoleId
 * @property adminRoleLevel
 * @constructor Create empty Db selected application
 */

@Entity(tableName = "selected_application")
data class dbSelectedApplication(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val secret: String,
    val version: String,
    val status: Int,
    val developerMode: Int,
    val twoFactorAuth: Int,
    val hwidCheck: Int,
    val freeMode: Int,
    val integrityCheck: Int,
    val programHash: String?,
    val downloadLink: String?,
    val adminRoleId: Int?,
    val adminRoleLevel: Int?,
)

/**
 * As db selected application
 *
 * @return
 */
fun Application.asDbSelectedApplication(): dbSelectedApplication {
    return dbSelectedApplication(
        id = this.id,
        name = this.name,
        secret = this.secret,
        version = this.version,
        status = this.status,
        developerMode = this.developerMode,
        twoFactorAuth = this.twoFactorAuth,
        hwidCheck = this.hwidCheck,
        freeMode = this.freeMode,
        integrityCheck = this.integrityCheck,
        programHash = this.programHash,
        downloadLink = this.downloadLink,
        adminRoleId = this.adminRoleId,
        adminRoleLevel = this.adminRoleLevel,
    )
}

/**
 * As application
 *
 * @param account
 * @return
 */
fun dbSelectedApplication.asApplication(account: Account): Application {
    return Application(
        id = this.id,
        name = this.name,
        secret = this.secret,
        version = this.version,
        status = this.status,
        developerMode = this.developerMode,
        twoFactorAuth = this.twoFactorAuth,
        hwidCheck = this.hwidCheck,
        freeMode = this.freeMode,
        integrityCheck = this.integrityCheck,
        programHash = this.programHash,
        downloadLink = this.downloadLink,
        adminRoleId = this.adminRoleId,
        adminRoleLevel = this.adminRoleLevel,
        account = AccountOfApp(
            id = account.account.id,
            name = account.account.username
        )
    )
}