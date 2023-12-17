package com.example.blitzware_android.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: dbAccount)

    @Update
    suspend fun update(account: dbAccount)

    @Delete
    suspend fun delete(account: dbAccount)

    @Query("DELETE FROM accounts")
    suspend fun deleteAll()

    @Query("SELECT * from accounts LIMIT 1")
    fun getAccount(): dbAccount
}