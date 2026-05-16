package com.example.mahilashaktiunnati

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DashboardScreen(this)
            }
        }
    }
}

@Composable
fun DashboardScreen(activity: DashboardActivity) {
    val context = LocalContext.current

    // State for Dynamic Summary Data
    var totalSavings by remember { mutableDoubleStateOf(0.0) }
    var totalLoans by remember { mutableDoubleStateOf(0.0) }

    // Logic to reload data every time the user returns to the Dashboard
    LaunchedEffect(Unit) {
        // Load Savings Total from SavingStorage
        val savingsList = SavingStorage.load(context)
        totalSavings = savingsList.sumOf { it.amount }

        // Load Loan Totals from LoanStorage
        val loansList = LoanStorage.loadLoans(context)
        totalLoans = loansList.sumOf { it.amount }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
    ) {
        // --- PURPLE HEADER SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6A1B9A))
                .padding(top = 40.dp, bottom = 30.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Mahila Shakti Unnati", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Member ID: MSU1001 | Group: Shakti SHG",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
                // NEW: Profile Icon that leads to ProfileActivity
                IconButton(onClick = {
                    activity.startActivity(Intent(activity, ProfileActivity::class.java))
                }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(35.dp))
                }
            }
        }

        // --- DYNAMIC STATISTICS ROW ---
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryStatCard(
                label = "Total Savings",
                value = "₹ ${String.format("%.2f", totalSavings)}",
                bgColor = Color(0xFFE8F5E9),
                textColor = Color(0xFF2E7D32),
                modifier = Modifier.weight(1f)
            )
            SummaryStatCard(
                label = "Active Loans",
                value = "₹ ${String.format("%.2f", totalLoans)}",
                bgColor = Color(0xFFFFEBEE),
                textColor = Color(0xFFC62828),
                modifier = Modifier.weight(1f)
            )
        }

        // --- QUICK ACTIONS GRID ---
        Text(
            text = "Quick Actions",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ActionItem(Icons.Default.AddCard, "Add Savings", Color(0xFF6A1B9A)) {
                    activity.startActivity(Intent(activity, SavingEntryActivity::class.java))
                }
                ActionItem(Icons.Default.History, "Savings History", Color(0xFF1565C0)) {
                    activity.startActivity(Intent(activity, SavingHistoryActivity::class.java))
                }
                ActionItem(Icons.Default.Group, "Directory", Color(0xFFEF6C00)) {
                    activity.startActivity(Intent(activity, MemberDirectoryActivity::class.java))
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ActionItem(Icons.Default.Description, "Request Loan", Color(0xFF2E7D32)) {
                    activity.startActivity(Intent(activity, LoanRequestActivity::class.java))
                }
                ActionItem(Icons.Default.Assessment, "Reports", Color(0xFF455A64)) {
                    activity.startActivity(Intent(activity, ReportsActivity::class.java))
                }
                ActionItem(Icons.Default.Settings, "Settings", Color(0xFF757575)) {
                    activity.startActivity(Intent(activity, SettingsActivity::class.java))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- LOGOUT BUTTON ---
        Button(
            onClick = {
                val session = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
                session.edit().clear().apply()
                activity.startActivity(Intent(activity, MainActivity::class.java))
                activity.finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Logout Session", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun SummaryStatCard(label: String, value: String, bgColor: Color, textColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, fontSize = 12.sp, color = Color.Black.copy(alpha = 0.6f))
            Text(text = value, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(18.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    }
}