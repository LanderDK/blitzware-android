package com.example.blitzware_android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ApplicationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(application: dbSelectedApplication)

    @Update
    suspend fun update(application: dbSelectedApplication)

    @Query("DELETE FROM selected_application")
    suspend fun deleteAll()


    @Query("SELECT * from selected_application LIMIT 1")
    fun getSelectedApplication(): dbSelectedApplication
}