package com.example.kotlin.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.model.BookingModel
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.repository.UserRepolmpl // Add this
import com.example.kotlin.ui.theme.PrimaryOrange

@Composable
fun UserBookingsScreen() {
    val repo = RestaurantRepoImpl()
    val userRepo = UserRepolmpl()
    val currentUserEmail = userRepo.getCurrentUser()?.email

    var myBookings by remember { mutableStateOf<List<BookingModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        repo.fetchAllBookings { list, success, _ ->
            if (success && list != null) {
                // FILTER: Only show bookings belonging to the current user
                myBookings = list.filter { it.userEmail == currentUserEmail }
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text("My Reservations", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (myBookings.isEmpty()) {
            item {
                Box(Modifier.fillParentMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No bookings found.", color = Color.Gray)
                }
            }
        }

        items(myBookings) { booking ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(booking.restaurantName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Date: ${booking.date} | Time: ${booking.time}", color = Color.Gray)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Status: ${booking.status}",
                            color = if(booking.status == "Approved") Color(0xFF4CAF50) else PrimaryOrange,
                            fontWeight = FontWeight.Medium
                        )
                        if(!booking.tableNo.isNullOrEmpty()) {
                            Text("Table: ${booking.tableNo}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}