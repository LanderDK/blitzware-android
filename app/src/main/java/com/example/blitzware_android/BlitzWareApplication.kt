package com.example.blitzware_android

import android.app.Application
import com.example.blitzware_android.data.AppContainer
import com.example.blitzware_android.data.DefaultAppContainer

class BlitzWareApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
