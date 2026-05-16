package com.example.mahilashaktiunnati

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class ReportsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ReportsScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Management for Dates
    var fromDate by remember { mutableStateOf("01/05/2026") }
    var toDate by remember { mutableStateOf("31/05/2026") }
    var selectedReport by remember { mutableStateOf("Savings Summary") }

    // Summary Data States
    var totalSavingsValue by remember { mutableStateOf("₹ 0.00") }
    var totalMembersCount by remember { mutableStateOf("0") }
    var totalGroupsCount by remember { mutableStateOf("0") }

    // DatePicker Logic
    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected("$dayOfMonth/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        TopAppBar(
            title = { Text("Group Reports", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = {
                    val shareBody = "Mahila Shakti Unnati - Summary Report\n" +
                            "Period: $fromDate to $toDate\n" +
                            "Total Members: $totalMembersCount\n" +
                            "Total Savings: $totalSavingsValue"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareBody)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Report"))
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
            Text("Select Report Type", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = selectedReport,
                onValueChange = { selectedReport = it },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("From Date", fontSize = 14.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = fromDate,
                        onValueChange = { },
                        modifier = Modifier.clickable { showDatePicker { fromDate = it } },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray
                        ),
                        trailingIcon = { Icon(Icons.Default.CalendarMonth, null) }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("To Date", fontSize = 14.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = toDate,
                        onValueChange = { },
                        modifier = Modifier.clickable { showDatePicker { toDate = it } },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray
                        ),
                        trailingIcon = { Icon(Icons.Default.CalendarMonth, null) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Functional Generate Button using SavingStorage
            Button(
                onClick = {
                    // Load real data from storage
                    val savingsData = SavingStorage.load(context)

                    // Perform real-time calculations
                    val sum = savingsData.sumOf { it.amount }
                    val uniqueMembers = savingsData.map { it.memberName }.distinct().size

                    // Update UI states
                    totalSavingsValue = "₹ ${String.format("%.2f", sum)}"
                    totalMembersCount = uniqueMembers.toString()
                    totalGroupsCount = if (uniqueMembers > 0) "1" else "0"

                    Toast.makeText(context, "Calculations Updated", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("GENERATE REPORT", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(30.dp))
            HorizontalDivider(thickness = 2.dp, color = Color(0xFFF3E5F5))

            Text(
                "SHG Summary",
                modifier = Modifier.padding(vertical = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF6A1B9A)
            )

            // Results Display Rows
            ReportRowItem("Total Savings", totalSavingsValue)
            ReportRowItem("Total Members Participated", totalMembersCount)
            ReportRowItem("Total Groups", totalGroupsCount)
        }
    }
}

@Composable
fun ReportRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.DarkGray, fontSize = 15.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
}