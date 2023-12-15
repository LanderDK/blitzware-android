package com.example.blitzware_android.network

import com.example.blitzware_android.model.File
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FileApiService {
    @GET("files/app/{applicationId}")
    suspend fun getFilesOfApplication(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String
    ): List<File>

    // TODO: werkt niet
    @POST("files/upload/{applicationId}")
    suspend fun createFile(
        @Header("Authorization") authorizationHeader: String,
        @Path("applicationId") applicationId: String,
        @Body body: Unit
    ): File

    @DELETE("files/{id}")
    suspend fun deleteFileById(
        @Header("Authorization") authorizationHeader: String,
        @Path("id") id: String
    ): Response<Unit>
}