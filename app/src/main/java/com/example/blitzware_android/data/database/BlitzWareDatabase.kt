package com.example.blitzware_android.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Blitz ware database
 *
 * @constructor Create empty Blitz ware database
 */
@Database(entities = [dbAccount::class, dbSelectedApplication::class], version = 2, exportSchema = false)
abstract class BlitzWareDatabase : RoomDatabase() {

    /**
     * Account dao
     *
     * @return
     */
    abstract fun accountDao(): AccountDao

    /**
     * Application dao
     *
     * @return
     */
    abstract fun applicationDao(): ApplicationDao

    companion object {
        @Volatile
        private var Instance: BlitzWareDatabase? = null

        fun getDatabase(context: Context): BlitzWareDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BlitzWareDatabase::class.java, "blitzware_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}