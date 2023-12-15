package com.example.blitzware_android.data

import com.example.blitzware_android.model.File
import com.example.blitzware_android.network.FileApiService

interface FileRepository {
    suspend fun getFilesOfApplication(
        token: String,
        applicationId: String
    ): List<File>

    suspend fun deleteFileById(
        token: String,
        id: String
    )
}

class NetworkFileRepository(
    private val fileApiService: FileApiService
) : FileRepository {
    override suspend fun getFilesOfApplication(token: String, applicationId: String): List<File> {
        val authorizationHeader = "Bearer $token"
        return fileApiService.getFilesOfApplication(authorizationHeader, applicationId)
    }

    override suspend fun deleteFileById(token: String, id: String) {
        val authorizationHeader = "Bearer $token"
        fileApiService.deleteFileById(authorizationHeader, id)
    }

}