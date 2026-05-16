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
import androidx.compose.material.icons.filled.Person
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

class LoanRequestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoanRequestScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanRequestScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Variables
    var selectedMember by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("2.0") }
    var durationWeeks by remember { mutableStateOf("10") }
    var purpose by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    // Calculation Logic
    val p = amount.toDoubleOrNull() ?: 0.0
    val r = interestRate.toDoubleOrNull() ?: 0.0
    val t = durationWeeks.toIntOrNull() ?: 0

    val durationInMonths = t / 4.0
    val totalInterest = (p * r * durationInMonths) / 100
    val totalPayable = p + totalInterest
    val weeklyEMI = if (t > 0) totalPayable / t else 0.0

    // Get current date for the record
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = { Text("Request Loan", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Member Name", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = selectedMember,
                onValueChange = { selectedMember = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter member name...") },
                trailingIcon = { Icon(Icons.Default.Person, null) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Loan Amount (₹)", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter Amount") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Monthly Interest Rate (%)", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = interestRate,
                onValueChange = { interestRate = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Repayment Duration (Weeks)", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = durationWeeks,
                onValueChange = { durationWeeks = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Interest Calculation Preview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Repayment Summary", fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A))
                    Spacer(Modifier.height(8.dp))

                    CalculationRow("Total Interest", "₹ ${String.format("%.2f", totalInterest)}")
                    CalculationRow("Total Payable", "₹ ${String.format("%.2f", totalPayable)}")
                    CalculationRow("Weekly EMI", "₹ ${String.format("%.2f", weeklyEMI)}")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Purpose", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = purpose,
                onValueChange = { purpose = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Business, Health") },
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Remarks", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                placeholder = { Text("Additional notes...") },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(24.dp))

            // SUBMIT LOGIC
            Button(
                onClick = {
                    if (selectedMember.isNotBlank() && amount.isNotBlank()) {

                        // 1. Create a new LoanEntry object
                        val newLoan = LoanEntry(
                            id = UUID.randomUUID().toString(),
                            memberName = selectedMember,
                            amount = p,
                            interestRate = r,
                            durationWeeks = t,
                            totalPayable = totalPayable,
                            remainingBalance = totalPayable,
                            status = "Active",
                            date = currentDate
                        )

                        // 2. Save to LoanStorage using our helper function
                        LoanStorage.addLoan(context, newLoan)

                        Toast.makeText(context, "Loan Submitted Successfully", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        Toast.makeText(context, "Please enter name and amount", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SUBMIT REQUEST", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun CalculationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color.DarkGray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}