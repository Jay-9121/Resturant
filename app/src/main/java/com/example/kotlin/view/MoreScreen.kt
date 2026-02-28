package com.example.kotlin.view

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.R
import com.example.kotlin.model.UserModel
import com.example.kotlin.repository.UserRepolmpl
import com.example.kotlin.ui.theme.PrimaryOrange
import com.example.kotlin.ui.theme.SoftBackground
import com.example.kotlin.ui.theme.White
import com.example.kotlin.viewmodel.UserViewModel

@Composable
fun MoreScreen() {
    val context = LocalContext.current
    val repo = remember { UserRepolmpl() }
    val viewModel = remember { UserViewModel(repo) }
    val userData by viewModel.userData.observeAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val currentUser = viewModel.getCurrentUser()
        if (currentUser?.email == "admin@gmail.com") {
            firstName = "Admin"; lastName = "User"; email = "admin@gmail.com"; isLoading = false
        } else {
            viewModel.getCurrentUserData()
        }
    }

    LaunchedEffect(userData) {
        userData?.let {
            firstName = it.firstName; lastName = it.lastName; email = it.email
            dob = it.dob; gender = it.gender; isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SoftBackground).padding(20.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Person, null, modifier = Modifier.size(100.dp), tint = PrimaryOrange)
        Text("Profile Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator(color = PrimaryOrange)
        } else {
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = email, onValueChange = { }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), enabled = false)

            Spacer(modifier = Modifier.height(20.dp))

            if (email != "admin@gmail.com") {
                Button(
                    onClick = {
                        val uid = viewModel.getCurrentUser()?.uid ?: ""
                        viewModel.updateProfile(uid, UserModel(uid, email, firstName, lastName, dob, gender)) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                ) { Text("Save Changes", color = White) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // NEW: Saved Restaurants Button
        OutlinedButton(
            onClick = { context.startActivity(Intent(context, SavedRestaurantsActivity::class.java)) },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryOrange)
        ) {
            Icon(painterResource(R.drawable.baseline_notifications_24), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Saved Restaurants")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.logout()
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                (context as ComponentActivity).finish()
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) { Text("Logout", color = White) }
    }
}