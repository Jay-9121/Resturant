package com.example.kotlin.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.kotlin.R
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.ui.theme.PrimaryOrange
import com.example.kotlin.ui.theme.White
import com.example.kotlin.viewmodel.RestaurantViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Makes status bar transparent
        setContent {
            AdminDashboardBody()
        }
    }
}

@Composable
fun AdminDashboardBody() {
    val context = LocalContext.current
    val repo = remember { RestaurantRepoImpl() }
    val viewModel = remember { RestaurantViewModel(repo) }

    var selectedIndex by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        Pair("Restaurants", R.drawable.baseline_home_24),
        Pair("Requests", R.drawable.baseline_notifications_24),
        Pair("Logout", R.drawable.baseline_logout_24)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = White) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            if (item.first == "Logout") {
                                // FIXED LOGOUT LOGIC: Clear backstack and redirect to Login
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                (context as ComponentActivity).finish()
                            } else {
                                selectedIndex = index
                            }
                        },
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
        },
        floatingActionButton = {
            // Only show the "Add" button when on the Restaurants list tab
            if (selectedIndex == 0) {
                FloatingActionButton(
                    onClick = {
                        context.startActivity(Intent(context, AddRestaurantActivity::class.java))
                    },
                    containerColor = PrimaryOrange
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add Restaurant",
                        tint = White
                    )
                }
            }
        }
    ) { padding ->
        // Use the padding values from Scaffold to prevent content overlap
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedIndex) {
                0 -> AdminRestaurantListScreen(viewModel)
                1 -> AdminBookingScreen()
            }
        }
    }
}