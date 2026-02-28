package com.example.kotlin.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlin.viewmodel.RestaurantViewModel

@Composable
fun SearchScreen(viewModel: RestaurantViewModel) {
    var query by remember { mutableStateOf("") }
    val restaurants by viewModel.restaurants
    val context = LocalContext.current

    // Filter logic: checks if name or cuisine matches the search query
    val filtered = restaurants.filter {
        it.name.contains(query, ignoreCase = true) ||
                it.cuisine.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search by name or cuisine...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { restaurant ->
                // FIXED: Pass both the onClick (for booking) and onSaveClick (for bookmarking)
                UserRestaurantCard(
                    restaurant = restaurant,
                    onClick = {
                        // Logic to open booking can be added here
                        Toast.makeText(context, "Opening booking for ${restaurant.name}", Toast.LENGTH_SHORT).show()
                    },
                    onSaveClick = { restaurantId ->
                        // Logic to save/bookmark
                        Toast.makeText(context, "Saved ${restaurant.name}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}