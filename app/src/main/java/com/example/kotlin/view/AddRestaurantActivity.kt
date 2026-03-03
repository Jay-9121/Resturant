package com.example.kotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag // Required import for testing
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.model.RestaurantModel
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.ui.theme.*
import com.example.kotlin.viewmodel.RestaurantViewModel

class AddRestaurantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinTheme { // Wrapped in theme for consistency
                AddRestaurantBody()
            }
        }
    }
}

@Composable
fun AddRestaurantBody() {
    val context = LocalContext.current

    // In a real app, use Hilt or factory, but keeping your logic for now
    val repo = remember { RestaurantRepoImpl() }
    val viewModel = remember { RestaurantViewModel(repo) }

    var name by remember { mutableStateOf("") }
    var cuisine by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var deal by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(SoftBackground)) {
        // Custom Header
        Surface(color = PrimaryOrange, shadowElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (context as ComponentActivity).finish() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                }
                Text(
                    text = "Post New Restaurant",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    // MATCHES: testHeaderIsVisible
                    modifier = Modifier.testTag("app_header")
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Restaurant Name") },
                    // MATCHES: testAddRestaurantFormFieldsVisible & testRestaurantNameInputTyping
                    modifier = Modifier.fillMaxWidth().testTag("name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, focusedLabelColor = PrimaryOrange)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = cuisine, onValueChange = { cuisine = it },
                    label = { Text("Cuisine Type") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, focusedLabelColor = PrimaryOrange)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = location, onValueChange = { location = it },
                    label = { Text("Location") },
                    // MATCHES: testAddRestaurantFormFieldsVisible ("address_input")
                    modifier = Modifier.fillMaxWidth().testTag("address_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, focusedLabelColor = PrimaryOrange)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = deal, onValueChange = { deal = it },
                    label = { Text("Deals & Offers (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, focusedLabelColor = PrimaryOrange)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isEmpty() || cuisine.isEmpty() || location.isEmpty()) {
                            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val model = RestaurantModel(name = name, cuisine = cuisine, location = location, deal = deal)
                        viewModel.addRestaurant(model) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) (context as ComponentActivity).finish()
                        }
                    },
                    // MATCHES: testSaveButtonIsPresent
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .testTag("save_restaurant_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) {
                    Text("Save Restaurant", color = White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RestaurantAdminCard(restaurant: RestaurantModel, onDelete: () -> Unit) {
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
                Text("${restaurant.cuisine} • ${restaurant.location}", style = MaterialTheme.typography.bodySmall, color = SecondaryGrey)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}