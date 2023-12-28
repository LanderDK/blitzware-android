package com.example.blitzware_android.network

import com.example.blitzware_android.model.File
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * File api service
 *
 * @constructor Create empty File api service
 */
interface FileApiService {
    /**
     * Get files of application
     *
     * @param authorizationHeader
     * @param applicationId
     * @return
     */
    @GET("files/app/{applicationId}")
    suspend fun getFilesOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<File>

    /**
     * Create file
     *
     * @param authorizationHeader
     * @param applicationId
     * @param body
     * @return
     */
    // TODO: werkt niet
    @POST("files/upload/{applicationId}")
    suspend fun createFile(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String,
        @Body body: Unit
    ): File

    /**
     * Delete file by id
     *
     * @param authorizationHeader
     * @param id
     * @return
     */
    @DELETE("files/{id}")
    suspend fun deleteFileById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}