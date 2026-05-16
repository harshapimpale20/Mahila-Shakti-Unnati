package com.example.mahilashaktiunnati

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingHistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    // State to hold the full list of savings from storage
    val allSavings = remember { mutableStateListOf<SavingEntry>() }

    // State for the Dropdown filter
    var expanded by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf("All Members") }

    // 1. DATA REFRESH LOGIC
    // This pulls the latest data, including names you typed randomly in the entry screen
    LaunchedEffect(Unit) {
        val loadedData = SavingStorage.load(context)
        allSavings.clear()
        allSavings.addAll(loadedData)
    }

    // 2. DYNAMIC FILTERING
    // Automatically updates the list and total when a different member is selected
    val filteredList = remember(allSavings, selectedMember) {
        if (selectedMember == "All Members") {
            allSavings
        } else {
            allSavings.filter { it.memberName == selectedMember }
        }
    }

    // Dynamic total based on the filtered view
    val totalAmount = filteredList.sumOf { it.amount }

    // 3. DYNAMIC MEMBER LIST
    // This creates a dropdown list containing "All Members" plus every unique name
    // that has ever been saved in your app.
    val memberNames = remember(allSavings) {
        listOf("All Members") + allSavings.map { it.memberName }.distinct().sorted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP APP BAR
        TopAppBar(
            title = {
                Text("Savings History", color = Color.White, fontWeight = FontWeight.Medium)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF6A1B9A) // Purple Branding
            ),
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {

            // MEMBER FILTER DROPDOWN
            Text(text = "Filter by Member", fontSize = 14.sp, color = Color.Gray)
            Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                OutlinedTextField(
                    value = selectedMember,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        disabledTrailingIconColor = Color.Gray
                    ),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    memberNames.forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                selectedMember = name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // TOTAL SAVINGS DISPLAY
            Text(text = "Total Savings", fontSize = 16.sp, color = Color.DarkGray)
            Text(
                text = "₹ ${String.format("%.2f", totalAmount)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32) // Success Green
            )

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

            // TRANSACTION LIST
            // This section displays the member name for every record so you can verify the data
            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No records found", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredList) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.date,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = item.memberName,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹ ${String.format("%.2f", item.amount)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = item.paymentType,
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }
                }
            }
        }
    }
}