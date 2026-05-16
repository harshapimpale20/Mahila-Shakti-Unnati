package com.example.mahilashaktiunnati

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray

class GroupDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GroupDetailsScreen { finish() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // State for dynamic data
    var totalMembers by remember { mutableIntStateOf(0) }
    var totalSavings by remember { mutableDoubleStateOf(0.0) }

    // Fetching real data from storage
    LaunchedEffect(Unit) {
        // Load Members from MEMBER_DATA
        val memberPrefs = context.getSharedPreferences("MEMBER_DATA", Context.MODE_PRIVATE)
        val savedMembers = memberPrefs.getString("members", null)
        if (!savedMembers.isNullOrEmpty()) {
            totalMembers = JSONArray(savedMembers).length()
        }

        // Load Savings from SavingStorage
        val savingsList = SavingStorage.load(context)
        totalSavings = savingsList.sumOf { it.amount }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F1FF)) // Light lavender background
    ) {
        // TOP APP BAR
        TopAppBar(
            title = { Text("Group Details", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Group Avatar Icon
            Surface(
                shape = CircleShape,
                color = Color(0xFF6A1B9A),
                modifier = Modifier.size(90.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Group Branding
            Text(
                text = "Shakti Self Help Group",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A)
            )
            Text(
                text = "Village: Rampur | Raipur",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(30.dp))

            // INFORMATION CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Group Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(16.dp))

                    DetailRow("Block", "Bhatapara")
                    DetailRow("District", "Raipur")
                    DetailRow("Total Members", totalMembers.toString())
                    DetailRow("Total Savings", "₹ $totalSavings", Color(0xFF2E7D32))
                    DetailRow("Total Loans", "₹ 15,000", Color.Red) // Hardcoded per reference
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            fontSize = 16.sp
        )
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
}