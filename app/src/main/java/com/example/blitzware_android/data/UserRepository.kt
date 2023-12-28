package com.example.blitzware_android.data

import com.example.blitzware_android.model.CreateUserBody
import com.example.blitzware_android.model.UpdateUserBody
import com.example.blitzware_android.model.User
import com.example.blitzware_android.network.UserApiService

/**
 * User repository
 *
 * @constructor Create empty User repository
 */
interface UserRepository {
    /**
     * Get users of application
     *
     * @param token
     * @param applicationId
     * @return
     */
    suspend fun getUsersOfApplication(
        token: String,
        applicationId: String
    ): List<User>

    /**
     * Create user from dashboard
     *
     * @param token
     * @param body
     * @return
     */
    suspend fun createUserFromDashboard(
        token: String,
        body: CreateUserBody
    ): User

    /**
     * Update user by id
     *
     * @param token
     * @param id
     * @param body
     */
    suspend fun updateUserById(
        token: String,
        id: String,
        body: UpdateUserBody
    )

    /**
     * Delete user by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteUserById(
        token: String,
        id: String
    )
}

/**
 * Network user repository
 *
 * @property userApiService
 * @constructor Create empty Network user repository
 */
class NetworkUserRepository(
    private val userApiService: UserApiService
) : UserRepository {
    override suspend fun getUsersOfApplication(token: String, applicationId: String): List<User> {
        val authorizationHeader = "Bearer $token"
        return userApiService.getUsersOfApplication(authorizationHeader, applicationId)
    }

    override suspend fun createUserFromDashboard(token: String, body: CreateUserBody): User {
        val authorizationHeader = "Bearer $token"
        return userApiService.createUserFromDashboard(authorizationHeader, body)
    }

    override suspend fun updateUserById(token: String, id: String, body: UpdateUserBody) {
        val authorizationHeader = "Bearer $token"
        userApiService.updateUserById(authorizationHeader, id, body)
    }

    override suspend fun deleteUserById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        userApiService.deleteUserById(authorizationHeader, id)
    }
}