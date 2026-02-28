package com.example.kotlin.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.model.RestaurantModel
import com.example.kotlin.ui.theme.*
import com.example.kotlin.viewmodel.RestaurantViewModel

@Composable
fun AdminRestaurantListScreen(viewModel: RestaurantViewModel) {
    val context = LocalContext.current
    val restaurantList by viewModel.restaurants
    val isLoading by viewModel.isLoading

    // State for the restaurant currently being edited
    var editingRestaurant by remember { mutableStateOf<RestaurantModel?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(SoftBackground)) {
        Surface(color = PrimaryOrange, shadowElevation = 4.dp) {
            Text(
                "Manage Restaurants", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(20.dp)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryOrange)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {
                items(restaurantList) { restaurant ->
                    RestaurantAdminCard(
                        restaurant = restaurant,
                        onDelete = {
                            viewModel.deleteRestaurant(restaurant.id) { _, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onEdit = { editingRestaurant = restaurant }
                    )
                }
            }
        }
    }

    // Logic to show Update Dialog when a restaurant is selected
    editingRestaurant?.let { restaurant ->
        UpdateRestaurantDialog(
            restaurant = restaurant,
            onDismiss = { editingRestaurant = null },
            onUpdate = { updatedFields ->
                viewModel.updateRestaurant(restaurant.id, updatedFields) { success, msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    if (success) editingRestaurant = null
                }
            }
        )
    }
}

@Composable
fun RestaurantAdminCard(
    restaurant: RestaurantModel,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, color = DarkGreyText)
                Text("${restaurant.cuisine} • ${restaurant.location}", color = SecondaryGrey, fontSize = 12.sp)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PrimaryOrange)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun UpdateRestaurantDialog(
    restaurant: RestaurantModel,
    onDismiss: () -> Unit,
    onUpdate: (Map<String, Any?>) -> Unit
) {
    var name by remember { mutableStateOf(restaurant.name) }
    var cuisine by remember { mutableStateOf(restaurant.cuisine) }
    var location by remember { mutableStateOf(restaurant.location) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Restaurant Info", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = cuisine, onValueChange = { cuisine = it }, label = { Text("Cuisine") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpdate(mapOf("name" to name, "cuisine" to cuisine, "location" to location)) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
            ) { Text("Update") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
        }
    )
}