package com.example.blitzware_android.data

import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.model.UserSubBody
import com.example.blitzware_android.network.UserSubApiService

/**
 * User sub repository
 *
 * @constructor Create empty User sub repository
 */
interface UserSubRepository {
    /**
     * Get user subs of application
     *
     * @param token
     * @param applicationId
     * @return
     */
    suspend fun getUserSubsOfApplication(
        token: String,
        applicationId: String
    ): List<UserSub>

    /**
     * Create user sub
     *
     * @param token
     * @param body
     * @return
     */
    suspend fun createUserSub(
        token: String,
        body: UserSubBody
    ): UserSub

    /**
     * Update user sub by id
     *
     * @param token
     * @param id
     * @param body
     */
    suspend fun updateUserSubById(
        token: String,
        id: Int,
        body: UserSubBody
    )

    /**
     * Delete user sub by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteUserSubById(
        token: String,
        id: Int
    )
}

/**
 * Network user sub repository
 *
 * @property userSubApiService
 * @constructor Create empty Network user sub repository
 */
class NetworkUserSubRepository(
    private val userSubApiService: UserSubApiService
) : UserSubRepository {
    override suspend fun getUserSubsOfApplication(token: String, applicationId: String): List<UserSub> {
        val authorizationHeader = "Bearer $token"
        return userSubApiService.getUserSubsOfApplication(authorizationHeader, applicationId)
    }

    override suspend fun createUserSub(token: String, body: UserSubBody): UserSub {
        val authorizationHeader = "Bearer $token"
        return userSubApiService.createUserSub(authorizationHeader, body)
    }

    override suspend fun updateUserSubById(token: String, id: Int, body: UserSubBody) {
        val authorizationHeader = "Bearer $token"
        userSubApiService.updateUserSubById(authorizationHeader, id, body)
    }

    override suspend fun deleteUserSubById(token: String, id: Int) {
        val authorizationHeader = "Bearer $token"
        userSubApiService.deleteUserSubById(authorizationHeader, id)
    }
}