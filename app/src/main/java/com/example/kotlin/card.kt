package com.example.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin.R
import com.example.kotlin.ui.theme.CreamWhite
import com.example.kotlin.ui.theme.RixColor

class card : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting2()
        }
    }
}

@Composable
fun Greeting2() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = RixColor)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Card", fontSize = 40.sp, color = CreamWhite)
                Image(
                    painter = painterResource(R.drawable.images),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = "Welcome to the card", fontSize = 20.sp, color = CreamWhite)
            Spacer(
                modifier = Modifier
                    .height(height = 50.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Text", fontSize = 25.sp)
                }
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Address", fontSize = 25.sp)
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Text", fontSize = 25.sp)
                }
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Text", fontSize = 25.sp)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Text", fontSize = 25.sp)
                }
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Text", fontSize = 25.sp)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .padding(all = 10.dp)
                        .background(color = CreamWhite)
                ) {
                    Image(
                        painter = painterResource(R.drawable.images),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Settings", fontSize = 25.sp)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Greeting2()
}