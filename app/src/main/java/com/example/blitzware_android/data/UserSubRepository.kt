package com.example.blitzware_android.data

import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.model.UserSubBody
import com.example.blitzware_android.network.UserSubApiService

interface UserSubRepository {
    suspend fun getUserSubsOfApplication(
        token: String,
        applicationId: String
    ): List<UserSub>

    suspend fun createUserSub(
        token: String,
        body: UserSubBody
    ): UserSub

    suspend fun updateUserSubById(
        token: String,
        id: String,
        body: UserSubBody
    )

    suspend fun deleteUserSubById(
        token: String,
        id: String
    )
}

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

    override suspend fun updateUserSubById(token: String, id: String, body: UserSubBody) {
        val authorizationHeader = "Bearer $token"
        userSubApiService.updateUserSubById(authorizationHeader, id, body)
    }

    override suspend fun deleteUserSubById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        userSubApiService.deleteUserSubById(authorizationHeader, id)
    }
}