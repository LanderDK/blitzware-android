package com.example.blitzware_android.data

import com.example.blitzware_android.model.File
import com.example.blitzware_android.network.FileApiService

/**
 * File repository
 *
 * @constructor Create empty File repository
 */
interface FileRepository {
    /**
     * Get files of application
     *
     * @param token
     * @param applicationId
     * @return
     */
    suspend fun getFilesOfApplication(
        token: String,
        applicationId: String
    ): List<File>

    /**
     * Delete file by id
     *
     * @param token
     * @param id
     */
    suspend fun deleteFileById(
        token: String,
        id: String
    )
}

/**
 * Network file repository
 *
 * @property fileApiService
 * @constructor Create empty Network file repository
 */
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