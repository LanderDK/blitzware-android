package com.example.blitzware_android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Application dao
 *
 * @constructor Create empty Application dao
 */
@Dao
interface ApplicationDao {
    /**
     * Insert
     *
     * @param application
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(application: dbSelectedApplication)

    /**
     * Update
     *
     * @param application
     */
    @Update
    suspend fun update(application: dbSelectedApplication)

    /**
     * Delete all
     *
     */
    @Query("DELETE FROM selected_application")
    suspend fun deleteAll()


    /**
     * Get selected application
     *
     * @return
     */
    @Query("SELECT * from selected_application LIMIT 1")
    suspend fun getSelectedApplication(): dbSelectedApplication
}