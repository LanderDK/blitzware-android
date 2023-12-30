package com.example.blitzware_android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blitzware_android.data.database.ApplicationDao
import com.example.blitzware_android.data.database.BlitzWareDatabase
import com.example.blitzware_android.data.database.asApplication
import com.example.blitzware_android.data.database.asDbSelectedApplication
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.model.AccountData
import com.example.blitzware_android.model.AccountOfApp
import com.example.blitzware_android.model.Application
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Application dao test (this is a unit test)
 *
 * @constructor Create empty Application dao test
 */
@RunWith(AndroidJUnit4::class)
class ApplicationDaoTest {
    private lateinit var applicationDao: ApplicationDao
    private lateinit var blitzWareDatabase: BlitzWareDatabase

    private var acc1 = Account(
        account = AccountData(
            id = "dd694ec0-de07-4fd0-b55c-f4c8eeedb02b",
            username = "Dummy User 1",
            email = "dummyuser1@mail.com",
            roles = listOf("dummy_role_1", "dummy_role_2"),
            creationDate = "2021-10-10T10:10:10.000Z",
            profilePicture = null,
            emailVerified = 1,
            twoFactorAuth = 0,
            enabled = 1
        ),
        token = "alegitjwttoken"
    )
    private var acc2 = Account(
        account = AccountData(
            id = "b3e51850-851e-430a-9ba0-21c7b3543717",
            username = "Dummy User 2",
            email = "dummyuser2@mail.com",
            roles = listOf("dummy_role_1"),
            creationDate = "2022-12-06T11:40:12.000Z",
            profilePicture = null,
            emailVerified = 1,
            twoFactorAuth = 0,
            enabled = 1
        ),
        token = "alegitjwttoken"
    )
    private var app1 = Application(
        id = "9ab10e48-8f9e-4e09-a4ee-4d64e4aaf4ad",
        name = "Dummy App 1",
        secret = "insanelysecuresecretthatnoonecanguess",
        status = 1,
        hwidCheck = 1,
        developerMode = 0,
        integrityCheck = 0,
        freeMode = 1,
        twoFactorAuth = 0,
        version = "1.0.0",
        programHash = null,
        downloadLink = null,
        adminRoleId = null,
        adminRoleLevel = null,
        account = AccountOfApp(
            id = "dd694ec0-de07-4fd0-b55c-f4c8eeedb02b",
            name = "Dummy User 1",
        )
    )
    private var app2 = Application(
        id = "f888693f-938e-433a-804e-6878fd5bf28d",
        name = "Dummy App 2",
        secret = "insanelysecuresecretthatnoonecanguessever",
        status = 1,
        hwidCheck = 0,
        developerMode = 0,
        integrityCheck = 0,
        freeMode = 0,
        twoFactorAuth = 0,
        version = "6.1.5",
        programHash = null,
        downloadLink = null,
        adminRoleId = null,
        adminRoleLevel = null,
        account = AccountOfApp(
            id = "b3e51850-851e-430a-9ba0-21c7b3543717",
            name = "Dummy User 2",
        )
    )

    private suspend fun addOneAppToDb() {
        applicationDao.insert(app1.asDbSelectedApplication())
    }

    private suspend fun addTwoAppsToDb() {
        applicationDao.insert(app1.asDbSelectedApplication())
        applicationDao.deleteAll()
        applicationDao.insert(app2.asDbSelectedApplication())
    }

    /**
     * Create db
     *
     */
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        blitzWareDatabase = Room.inMemoryDatabaseBuilder(context, BlitzWareDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        applicationDao = blitzWareDatabase.applicationDao()
    }

    /**
     * Close db
     *
     */
    @After
    @Throws(Exception::class)
    fun closeDb() {
        blitzWareDatabase.close()
    }

    /**
     * Dao select an app_insert app into db
     *
     */
    @Test
    fun daoSelectAnApp_insertAppIntoDb() = runBlocking {
        addOneAppToDb()
        val selectedApp = applicationDao.getSelectedApplication()
        assertEquals(selectedApp.asApplication(acc1), app1)
    }

    /**
     * Dao select a new app_insert one app in db then delete then add new app in db
     *
     */
    @Test
    fun daoSelectANewApp_insertOneAppInDbThenDeleteThenAddNewAppInDb() = runBlocking {
        addTwoAppsToDb()
        val selectedApp = applicationDao.getSelectedApplication()
        assertFalse(selectedApp.asApplication(acc2) == app1)
        assertEquals(selectedApp.asApplication(acc2), app2)
    }
}