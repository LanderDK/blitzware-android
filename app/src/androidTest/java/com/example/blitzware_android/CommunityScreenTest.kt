package com.example.blitzware_android

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blitzware_android.ui.screens.CommunityScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Community screen test
 *
 * @constructor Create empty Community screen test
 */
@RunWith(AndroidJUnit4::class)
class CommunityScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Community screen_initial state
     *
     */
    @Test
    fun communityScreen_initialState() {
        composeTestRule.setContent {
            CommunityScreen()
        }

        composeTestRule.onNodeWithText("Community General Chat").assertExists()
        composeTestRule.onNodeWithContentDescription("Refresh").assertExists()
        composeTestRule.onNodeWithText("Loading messages...").assertDoesNotExist()
        composeTestRule.onNodeWithText("Type here, ...").assertExists()
        composeTestRule.onNodeWithContentDescription("Type here, ...").assertExists()
        composeTestRule.onNodeWithContentDescription("Send").assertDoesNotExist()
    }
}