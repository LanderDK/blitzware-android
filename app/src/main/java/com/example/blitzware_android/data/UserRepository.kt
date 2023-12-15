package com.example.blitzware_android.data

import com.example.blitzware_android.model.CreateUserBody
import com.example.blitzware_android.model.UpdateUserBody
import com.example.blitzware_android.model.User
import com.example.blitzware_android.network.UserApiService

interface UserRepository {
    suspend fun getUsersOfApplication(
        token: String,
        applicationId: String
    ): List<User>

    suspend fun createUserFromDashboard(
        token: String,
        body: CreateUserBody
    ): User

    suspend fun updateUserById(
        token: String,
        id: String,
        body: UpdateUserBody
    )

    suspend fun deleteUserById(
        token: String,
        id: String
    )
}

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