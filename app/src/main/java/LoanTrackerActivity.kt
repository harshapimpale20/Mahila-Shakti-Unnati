package com.example.mahilashaktiunnati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class LoanTrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoanTrackerScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanTrackerScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // State to hold live data from storage
    var loanList by remember { mutableStateOf(listOf<LoanEntry>()) }
    var totalLoanAmount by remember { mutableDoubleStateOf(0.0) }

    // Load data from LoanStorage
    LaunchedEffect(Unit) {
        val savedLoans = LoanStorage.loadLoans(context)
        loanList = savedLoans
        totalLoanAmount = savedLoans.sumOf { it.amount }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F1FF))
    ) {
        // TOP HEADER
        TopAppBar(
            title = { Text("Loan Tracker", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // TOTAL LOANS SUMMARY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Total Group Loans", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "₹ ${String.format("%.2f", totalLoanAmount)}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Member Loan Records",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // LIST OF REAL LOAN RECORDS
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(loanList) { loan ->
                    LoanRecordCard(loan)
                }
            }
        }
    }
}

@Composable
fun LoanRecordCard(loan: LoanEntry) {
    // Calculate progress (Mocked for UI as 40% paid, you can update this logic later)
    val paidAmount = loan.totalPayable - loan.remainingBalance
    val progress = if (loan.totalPayable > 0) (paidAmount / loan.totalPayable).toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = loan.memberName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = loan.status,
                        fontSize = 12.sp,
                        color = if (loan.status == "Approved") Color(0xFF2E7D32) else Color(0xFFEF6C00),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "₹ ${loan.amount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(12.dp))

            // REPAYMENT TRACKING (Requirement from image_b03036.png)
            Text(text = "Repayment Progress", fontSize = 11.sp, color = Color.Gray)
            LinearProgressIndicator(
                progress = { 0.4f }, // Placeholder for repayment tracking
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                color = Color(0xFF6A1B9A),
                trackColor = Color(0xFFF3E5F5)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Remaining: ₹ ${loan.remainingBalance}",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Int. Rate: ${loan.interestRate}%",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}