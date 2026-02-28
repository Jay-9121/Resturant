package com.example.kotlin.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.kotlin.repository.UserRepolmpl
import com.example.kotlin.ui.theme.Blue
import com.example.kotlin.ui.theme.PurpleGrey
import com.example.kotlin.viewmodel.UserViewModel


class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgetBody()
        }
    }
}

@Composable
fun ForgetBody() {
    val userViewModel = remember { UserViewModel(UserRepolmpl()) }
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val activity = context as ComponentActivity

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {

                OutlinedTextField(
                    value = email,
                    onValueChange = { data ->
                        email = data
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    placeholder = {
                        Text("abc@gmail.com")
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = PurpleGrey,
                        focusedContainerColor = PurpleGrey,
                        focusedIndicatorColor = Blue,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(15.dp)
                )


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        userViewModel.forgetPassword(email) { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
//                    if (localEmail == email && localPassword == password) {
//                        val intent = Intent(
//                            context, DashboardActivity::class.java
//                        )
//                        context.startActivity(intent)
//                        activity.finish()
//                    } else {
//                        Toast.makeText(context,
//                            "Invalid login",
//                            Toast.LENGTH_LONG).show()
//                    }

//
//                    intent.putExtra("email",email)
//                    intent.putExtra("password",password)
//


//                    showDialog = true
//                    if(email == "ram" && password == "password"){
//                        Toast.makeText(context,
//                            "Login successfully",
//                            Toast.LENGTH_LONG).show()
//                    }else{
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(
//                                "Invalid email or password",
//                                withDismissAction = true
//                            )
//                        }
//                    }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text("Forget Password")
                }
                Text(buildAnnotatedString {
                    append("Already have an account? ")

                    withStyle(SpanStyle(color = Blue)) {
                        append("Log in")
                    }
                }, modifier = Modifier.clickable{
                    val intent = Intent(
                        context,
                        LoginActivity::class.java)

                    context.startActivity(intent)
                    activity.finish()
                })
            }
        }
    }
}
