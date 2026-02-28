package com.example.kotlin.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.R
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.repository.UserRepolmpl
import com.example.kotlin.ui.theme.SoftBackground
import com.example.kotlin.viewmodel.RestaurantViewModel

class SavedRestaurantsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repo = remember { RestaurantRepoImpl() }
            val viewModel = remember { RestaurantViewModel(repo) }

            SavedRestaurantsScreen(
                viewModel = viewModel,
                onBack = { finish() }
            )
        }
    }
}

@Composable
fun SavedRestaurantsScreen(viewModel: RestaurantViewModel, onBack: () -> Unit) {
    val userRepo = remember { UserRepolmpl() }
    val currentUserId = userRepo.getCurrentUser()?.uid ?: ""

    val restaurants by viewModel.restaurants

    // FILTER: Compiles now because 'savedBy' is in the RestaurantModel
    val displayList = remember(restaurants) {
        restaurants.filter { it.savedBy.contains(currentUserId) }
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp, color = MaterialTheme.colorScheme.surface) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.baseline_arrow_back_24), "Back")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("My Saved Places", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(SoftBackground)
        ) {
            if (displayList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painterResource(R.drawable.baseline_bookmark_border_24), null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No saved restaurants found.", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(displayList) { restaurant ->
                        UserRestaurantCard(
                            restaurant = restaurant,
                            onClick = { /* Handle Booking */ },
                            onSaveClick = {
                                // Logic: Toggle save by passing ID and current list to repo
                                val repo = RestaurantRepoImpl()
                                repo.toggleSaveRestaurant(restaurant.id, currentUserId, restaurant.savedBy) { _, _ -> }
                            }
                        )
                    }
                }
            }
        }
    }
}