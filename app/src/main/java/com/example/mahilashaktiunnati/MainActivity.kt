package com.example.mahilashaktiunnati

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check Login Session
        val session = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
        val isLoggedIn = session.getBoolean("isLoggedIn", false)

        // Redirect if already authenticated
        if (isLoggedIn) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        setContent {
            MaterialTheme {
                LoginScreen(this)
            }
        }
    }
}

@Composable
fun LoginScreen(activity: MainActivity) {
    val context = activity
    val userData = activity.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

    // State variables for user modification
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F1FF))
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Branding
                Text(
                    text = "MAHILA SHAKTI UNNATI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Empowering Women Through Savings",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Mobile Number Input
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // LOGIN BUTTON
                Button(
                    onClick = {
                        if (mobile.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            val savedMobile = userData.getString("mobile", "9876543210") // Default for testing
                            val savedPassword = userData.getString("password", "password")

                            if (mobile == savedMobile && password == savedPassword) {
                                // Persist session
                                val session = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
                                session.edit().putBoolean("isLoggedIn", true).apply()

                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, DashboardActivity::class.java))
                                context.finish()
                            } else {
                                Toast.makeText(context, "Invalid Mobile or Password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "LOGIN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation to Register
                TextButton(
                    onClick = {
                        context.startActivity(Intent(context, RegisterActivity::class.java))
                    }
                ) {
                    Text(
                        text = "New User? Register Here",
                        color = Color(0xFF6A1B9A),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}