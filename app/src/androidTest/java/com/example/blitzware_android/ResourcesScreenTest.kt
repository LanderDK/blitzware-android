package com.example.blitzware_android

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blitzware_android.ui.screens.ResourcesScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Resources screen test
 *
 * @constructor Create empty Resources screen test
 */
@RunWith(AndroidJUnit4::class)
class ResourcesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Resources screen_initial state
     *
     */
    @Test
    fun resourcesScreen_initialState() {
        composeTestRule.setContent {
            ResourcesScreen()
        }

        composeTestRule.onNodeWithText("Available Resources").assertExists()
        composeTestRule.onNodeWithText("API Documentation").assertExists()
        composeTestRule.onNodeWithText("https://docs.blitzware.xyz/").assertExists()
        composeTestRule.onAllNodesWithContentDescription("Button to open link").assertCountEquals(7)
    }
}