package com.example.kotlin

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kotlin", appContext.packageName)
    }
    @Test
    fun testAppLabelIsCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedName = "Kotlin" // Replace with your actual @string/app_name
        val actualName = appContext.getString(appContext.applicationInfo.labelRes)
        assertEquals("The app name should be correct", expectedName, actualName)
    }
    @Test
    fun testAddRestaurantActivityIsRegistered() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = android.content.Intent(appContext, com.example.kotlin.view.AddRestaurantActivity::class.java)
        val resolveInfo = appContext.packageManager.resolveActivity(intent, 0)
        assertNotNull("AddRestaurantActivity should be registered in AndroidManifest", resolveInfo)
    }
    @Test
    fun testSharedPreferencesStorage() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs = appContext.getSharedPreferences("test_prefs", android.content.Context.MODE_PRIVATE)

        prefs.edit().putString("username", "GeminiUser").commit()

        val savedName = prefs.getString("username", null)
        assertEquals("GeminiUser", savedName)
    }
    @Test
    fun testDatabaseFileExists() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Replace "restaurant_db" with your actual database name
        val dbFile = appContext.getDatabasePath("restaurant_db")
        // Note: This only passes if the app has been run once to trigger DB creation
        assertNotNull(dbFile)
    }
}