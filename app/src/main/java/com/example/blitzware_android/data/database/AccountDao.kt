package com.example.blitzware_android.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Account dao
 *
 * @constructor Create empty Account dao
 */
@Dao
interface AccountDao {
    /**
     * Insert
     *
     * @param account
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: dbAccount)

    /**
     * Update
     *
     * @param account
     */
    @Update
    suspend fun update(account: dbAccount)

    /**
     * Delete
     *
     * @param account
     */
    @Delete
    suspend fun delete(account: dbAccount)

    /**
     * Delete all
     *
     */
    @Query("DELETE FROM accounts")
    suspend fun deleteAll()

    /**
     * Get account
     *
     * @return
     */
    @Query("SELECT * from accounts LIMIT 1")
    suspend fun getAccount(): dbAccount
}