package com.example.mahilashaktiunnati

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class SavingEntryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SavingEntryScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingEntryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Management for form inputs
    var memberName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }

    // Automatic date generation
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = { Text("Add Saving Entry", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Member Name Field
            Text("Member Name", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = memberName,
                onValueChange = { memberName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter full name") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Amount Field
            Text("Amount (₹)", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("0.00") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Mobile Number Field
            Text("Mobile Number", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter 10-digit number") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Transaction ID Field
            Text("Transaction ID (Optional)", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = transactionId,
                onValueChange = { transactionId = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ref ID for GPay/PhonePe") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(32.dp))

            // Save Button Logic
            Button(
                onClick = {
                    if (memberName.isNotBlank() && amount.isNotBlank()) {
                        // Creating the entry using the model defined in SavingStorage.kt
                        val newEntry = SavingEntry(
                            memberName = memberName,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            paymentType = if (transactionId.isEmpty()) "Cash" else "Digital",
                            status = "Paid",
                            date = currentDate,
                            mobileNumber = mobileNumber,
                            transactionId = transactionId
                        )

                        // Call to the centralized storage
                        SavingStorage.addEntry(context, newEntry)

                        Toast.makeText(context, "Entry Saved Successfully", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        Toast.makeText(context, "Please enter Name and Amount", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SAVE ENTRY", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}