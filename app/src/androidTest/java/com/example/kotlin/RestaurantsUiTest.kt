package com.example.kotlin

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.kotlin.ui.theme.KotlinTheme
import com.example.kotlin.view.AddRestaurantActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RestaurantsUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<AddRestaurantActivity>()

    @Test
    fun testAddRestaurantFormFieldsVisible() {
        // Wait for the idle state to ensure Compose is loaded
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("name_input").assertIsDisplayed()
        composeTestRule.onNodeWithTag("address_input").assertIsDisplayed()
    }

    @Test
    fun testSaveButtonIsPresent() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("save_restaurant_button").assertIsDisplayed()
    }

    @Test
    fun testRestaurantNameInputTyping() {
        composeTestRule.waitForIdle()
        val testName = "Tasty Burger"
        composeTestRule.onNodeWithTag("name_input").performTextInput(testName)
        composeTestRule.onNodeWithTag("name_input").assertTextContains(testName)
    }

    @Test
    fun testOrderButtonIsEnabled() {
        // For generic component tests, we manually set content
        composeTestRule.setContent {
            KotlinTheme {
                Button(
                    onClick = {},
                    modifier = Modifier.testTag("order_now_button_test")
                ) { Text("Order Now") }
            }
        }
        composeTestRule.onNodeWithTag("order_now_button_test").assertIsEnabled()
    }

    @Test
    fun testHeaderIsVisible() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("app_header").assertIsDisplayed()
    }
}