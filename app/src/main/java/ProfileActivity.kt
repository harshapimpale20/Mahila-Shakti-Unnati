package com.example.mahilashaktiunnati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProfileScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP HEADER
        TopAppBar(
            title = { Text("My Profile", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // PROFILE IMAGE CIRCLE
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color(0xFFF3E5F5)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(25.dp),
                    tint = Color(0xFF6A1B9A)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text("Savitri Devi", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Member ID: MSU1001", color = Color.Gray, fontSize = 14.sp)

            Spacer(Modifier.height(30.dp))

            // INFO CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F1FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileDetailRow("Full Name", "Savitri Devi")
                    ProfileDetailRow("Mobile Number", "9876543210")
                    ProfileDetailRow("Group Name", "Shakti Self Help Group")
                    ProfileDetailRow("Member Since", "10 May 2025")
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { /* Edit Action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("EDIT PROFILE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Divider(thickness = 0.5.dp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
    }
}