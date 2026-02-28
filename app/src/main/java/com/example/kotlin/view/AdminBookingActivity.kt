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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

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

    // TAB STATE: 0 = Pending, 1 = Approved, 2 = Declined
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pending", "Approved", "Declined")

    LaunchedEffect(Unit) {
        repo.fetchAllBookings { list, success, _ ->
            if (success && list != null) { bookings = list }
        }
    }

    // Filtered lists based on status
    val filteredList = when (selectedTab) {
        0 -> bookings.filter { it.status == "Pending" }
        1 -> bookings.filter { it.status == "Approved" }
        else -> bookings.filter { it.status == "Declined" }
    }

    Column(modifier = Modifier.fillMaxSize().background(SoftBackground)) {
        // --- HEADER ---
        Surface(color = PrimaryOrange, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.statusBarsPadding().padding(horizontal = 4.dp, vertical = 8.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { (context as ComponentActivity).finish() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                    }
                    Text("Manage Requests", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                // --- TABS (Like Job Portals) ---
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = PrimaryOrange,
                    contentColor = White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = White
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }

        // --- LIST VIEW ---
        if (filteredList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No ${tabs[selectedTab]} requests found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { booking ->
                    BookingApprovalCard(booking = booking) { status, tableNo ->
                        repo.updateBookingStatus(booking.bookingId, status, tableNo) { success, _ ->
                            if (success) {
                                repo.fetchAllBookings { list, _, _ -> if (list != null) bookings = list }
                                Toast.makeText(context, "Status updated to $status", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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
            Text("${booking.restaurantName}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkGreyText)
            Text("From: ${booking.userEmail}", color = Color.DarkGray, fontSize = 14.sp)
            Text("${booking.date} | ${booking.time}", color = PrimaryOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = SoftBackground)

            if (isPending) {
                OutlinedTextField(
                    value = tableInput,
                    onValueChange = { tableInput = it },
                    label = { Text("Assign Table (Optional)") },
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
                    ) { Text("Approve") }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onAction("Declined", "") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Decline") }
                }
            } else {
                // Status Badge for Approved/Declined
                Surface(
                    color = if (booking.status == "Approved") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "STATUS: ${booking.status} ${if (booking.tableNo.isNotEmpty()) " (Table ${booking.tableNo})" else ""}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (booking.status == "Approved") Color(0xFF2E7D32) else Color.Red
                    )
                }
            }
        }
    }
}