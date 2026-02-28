package com.example.kotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.model.BookingModel
import com.example.kotlin.repository.RestaurantRepoImpl
import com.example.kotlin.ui.theme.*

class AdminBookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AdminBookingScreen() }
    }
}

@Composable
fun AdminBookingScreen() {
    val repo = remember { RestaurantRepoImpl() }
    val context = LocalContext.current
    var bookings by remember { mutableStateOf<List<BookingModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        repo.fetchAllBookings { list, success, _ ->
            if (success && list != null) { bookings = list }
        }
    }

    // --- FILTER LOGIC ---
    val pendingBookings = bookings.filter { it.status == "Pending" }
    val historyBookings = bookings.filter { it.status != "Pending" }

    Column(modifier = Modifier.fillMaxSize().background(SoftBackground)) {
        Surface(color = PrimaryOrange, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.statusBarsPadding().padding(horizontal = 4.dp, vertical = 8.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { (context as ComponentActivity).finish() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                }
                Text("Booking Requests", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section 1: Active Requests
            if (pendingBookings.isNotEmpty()) {
                item { Text("New Requests", fontWeight = FontWeight.Bold, color = PrimaryOrange) }
                items(pendingBookings) { booking ->
                    BookingApprovalCard(booking = booking) { status, tableNo ->
                        repo.updateBookingStatus(booking.bookingId, status, tableNo) { success, _ ->
                            if (success) {
                                repo.fetchAllBookings { list, _, _ -> if (list != null) bookings = list }
                            }
                        }
                    }
                }
            }

            // Section 2: Processed History
            if (historyBookings.isNotEmpty()) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Text("History", fontWeight = FontWeight.Bold, color = Color.Gray) }
                items(historyBookings) { booking ->
                    BookingApprovalCard(booking = booking) { _, _ -> /* Actions locked */ }
                }
            }
        }
    }
}

@Composable
fun BookingApprovalCard(booking: BookingModel, onAction: (String, String) -> Unit) {
    var tableInput by remember { mutableStateOf(booking.tableNo) }
    val isPending = booking.status == "Pending"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Restaurant: ${booking.restaurantName}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreyText)
            Text("User: ${booking.userEmail}", color = Color.DarkGray, fontSize = 14.sp)
            Text("Time: ${booking.date} | ${booking.time}", color = PrimaryOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = SoftBackground)

            if (isPending) {
                OutlinedTextField(
                    value = tableInput,
                    onValueChange = { tableInput = it },
                    label = { Text("Assign Table No.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, focusedLabelColor = PrimaryOrange)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onAction("Approved", tableInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Approve", color = White) }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onAction("Declined", "") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Decline", color = White) }
                }
            } else {
                Text(
                    text = "Status: ${booking.status} ${if (booking.tableNo.isNotEmpty()) " (Table ${booking.tableNo})" else ""}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (booking.status == "Approved") Color(0xFF4CAF50) else Color.Red
                )
                Text("Action completed and locked.", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}