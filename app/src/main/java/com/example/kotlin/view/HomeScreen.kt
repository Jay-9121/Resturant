package com.example.kotlin.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.model.BookingModel
import com.example.kotlin.model.RestaurantModel
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.repository.UserRepolmpl
import com.example.kotlin.ui.theme.DarkGreyText
import com.example.kotlin.ui.theme.SoftBackground
import com.example.kotlin.viewmodel.RestaurantViewModel

@Composable
fun HomeScreen(viewModel: RestaurantViewModel) {
    val restaurants by viewModel.restaurants
    val context = LocalContext.current

    // Repositories for data operations
    val restaurantRepo = remember { RestaurantRepoImpl() }
    val userRepo = remember { UserRepolmpl() }

    // States for handling the booking selection
    var selectedRestaurant by remember { mutableStateOf<RestaurantModel?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Find Your Table",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreyText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // List of available restaurants
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(restaurants) { restaurant ->
                UserRestaurantCard(
                    restaurant = restaurant,
                    onClick = {
                        selectedRestaurant = restaurant
                        showDialog = true
                    },
                    onSaveClick = { restaurantId ->
                        // 1. Get current User ID
                        val currentUserId = userRepo.getCurrentUser()?.uid ?: ""

                        if (currentUserId.isNotEmpty()) {
                            // 2. Call the Repo to toggle the user ID in the savedBy list
                            restaurantRepo.toggleSaveRestaurant(
                                restaurantId = restaurantId,
                                userId = currentUserId,
                                currentList = restaurant.savedBy
                            ) { success, msg ->
                                // The UI will update automatically because viewModel.restaurants
                                // is observing the Firebase reference.
                                if (!success) {
                                    Toast.makeText(context, "Error: $msg", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please login to save places", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    // Booking Dialog
    if (showDialog && selectedRestaurant != null) {
        BookingDialog(
            restaurant = selectedRestaurant!!,
            onDismiss = { showDialog = false },
            onBook = { dateInput, timeInput ->
                val currentUserEmail = userRepo.getCurrentUser()?.email ?: ""

                val booking = BookingModel(
                    restaurantId = selectedRestaurant!!.id,
                    restaurantName = selectedRestaurant!!.name,
                    userEmail = currentUserEmail,
                    date = dateInput,
                    time = timeInput,
                    status = "Pending"
                )

                restaurantRepo.makeBooking(booking) { success, msg ->
                    if (success) {
                        Toast.makeText(context, "Booking Request Sent!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: $msg", Toast.LENGTH_SHORT).show()
                    }
                    showDialog = false
                }
            }
        )
    }
}