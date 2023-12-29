package com.example.blitzware_android

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blitzware_android.navigation.Screens
import com.example.blitzware_android.ui.BlitzWareApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * App navigation test
 *
 * @constructor Create empty App navigation test
 */
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var windowSize: WindowSizeClass

    /**
     * Setup nav host
     *
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            windowSize = calculateWindowSizeClass(LocalContext.current as Activity)
            BlitzWareApp(windowSize = windowSize.widthSizeClass, navController = navController)
        }
    }

    /**
     * Nav host_verify start destination
     *
     */
    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screens.AppsScreen.name)
    }
}