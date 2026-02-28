package com.example.kotlin.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.kotlin.model.RestaurantModel
import com.example.kotlin.repository.UserRepolmpl
import com.example.kotlin.ui.theme.PrimaryOrange
import com.example.kotlin.ui.theme.White

@Composable
fun UserRestaurantCard(
    restaurant: RestaurantModel,
    onClick: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    // We get the current user ID to check the saved status locally
    val userRepo = remember { UserRepolmpl() }
    val currentUserId = userRepo.getCurrentUser()?.uid ?: ""

    // Logic: If the user's ID is in the savedBy list, the icon should be filled
    val isSaved = restaurant.savedBy.contains(currentUserId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(restaurant.cuisine, color = Color.Gray, fontSize = 14.sp)
                Text(restaurant.location, color = PrimaryOrange, fontSize = 12.sp)
            }

            // Fixed Layout: Buttons in a Row to prevent overlapping
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = { onSaveClick(restaurant.id) }) {
                    Icon(
                        // Switch between filled and border icon based on isSaved status
                        painter = painterResource(
                            id = if (isSaved) R.drawable.baseline_bookmark_24
                            else R.drawable.baseline_bookmark_border_24
                        ),
                        contentDescription = "Save",
                        tint = PrimaryOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Book", color = White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BookingDialog(
    restaurant: RestaurantModel,
    onDismiss: () -> Unit,
    onBook: (String, String) -> Unit
) {
    var dateInput by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Book Table at ${restaurant.name}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Please provide your preferred timing:", fontSize = 14.sp, color = Color.Gray)

                OutlinedTextField(
                    value = dateInput,
                    onValueChange = { dateInput = it },
                    label = { Text("Date (e.g., 12 Dec)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = timeInput,
                    onValueChange = { timeInput = it },
                    label = { Text("Time (e.g., 8:00 PM)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onBook(dateInput, timeInput) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                enabled = dateInput.isNotEmpty() && timeInput.isNotEmpty()
            ) {
                Text("Confirm Booking", color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}