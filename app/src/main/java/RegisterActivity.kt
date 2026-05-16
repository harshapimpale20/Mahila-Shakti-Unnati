package com.example.mahilashaktiunnati

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(this)
        }
    }
}

@Composable
fun RegisterScreen(activity: RegisterActivity) {

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F1FF))
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "User Registration",
                    fontSize = 28.sp,
                    color = Color(0xFF6A1B9A)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        if (name.isEmpty() ||
                            mobile.isEmpty() ||
                            password.isEmpty()
                        ) {

                            Toast.makeText(
                                activity,
                                "Fill all fields",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            val sharedPreferences =
                                activity.getSharedPreferences(
                                    "USER_DATA",
                                    Context.MODE_PRIVATE
                                )

                            sharedPreferences.edit()
                                .putString("name", name)
                                .putString("mobile", mobile)
                                .putString("password", password)
                                .apply()

                            Toast.makeText(
                                activity,
                                "Registration Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            activity.startActivity(
                                Intent(
                                    activity,
                                    MainActivity::class.java
                                )
                            )

                            activity.finish()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A1B9A)
                    )
                ) {

                    Text("REGISTER")
                }
            }
        }
    }
}