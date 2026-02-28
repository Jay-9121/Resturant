// File: com.example.kotlin.view.UserDashboardActivity.kt
package com.example.kotlin.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.kotlin.R
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.ui.theme.PrimaryOrange
import com.example.kotlin.ui.theme.White
import com.example.kotlin.viewmodel.RestaurantViewModel

class UserDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserDashboardBody()
        }
    }
}

@Composable
fun UserDashboardBody() {
    // Initialize Repository and ViewModel
    val repo = remember { RestaurantRepoImpl() }
    val viewModel = remember { RestaurantViewModel(repo) }

    var selectedIndex by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        Pair("Home", R.drawable.baseline_home_24),
        Pair("Search", R.drawable.baseline_search_24),
        Pair("Bookings", R.drawable.baseline_notifications_24),
        Pair("Profile", R.drawable.baseline_settings_24)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = White) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(painterResource(item.second), contentDescription = null) },
                        label = { Text(item.first) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryOrange,
                            selectedTextColor = PrimaryOrange,
                            indicatorColor = PrimaryOrange.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedIndex) {
                0 -> HomeScreen(viewModel)      // Pass viewModel to HomeScreen
                1 -> SearchScreen(viewModel)    // Pass viewModel to SearchScreen
                2 -> UserBookingsScreen()       // Now in its own file
                3 -> MoreScreen()               // Now in its own file
            }
        }
    }
}