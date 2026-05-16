package com.example.mahilashaktiunnati

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SettingsScreen(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(activity: SettingsActivity) {
    val context = LocalContext.current

    // State for Password Dialog
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }

    // State for Language Selection
    var currentLanguage by remember { mutableStateOf("English") }

    // Change Password Dialog
    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password") },
            text = {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPassword.isNotEmpty()) {
                        Toast.makeText(context, "Password Updated!", Toast.LENGTH_SHORT).show()
                        showPasswordDialog = false
                    }
                }) { Text("Update") }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // TOP APP BAR
        TopAppBar(
            title = { Text("Settings", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { activity.finish() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
        )

        // SETTINGS LIST ITEMS
        SettingsItem(Icons.Default.Lock, "Change Password") {
            showPasswordDialog = true
        }

        SettingsItem(Icons.Default.Language, "Language", currentLanguage) {
            // Toggle logic for demo purposes
            currentLanguage = if (currentLanguage == "English") "Hindi" else "English"
            Toast.makeText(context, "Language changed to $currentLanguage", Toast.LENGTH_SHORT).show()
        }

        SettingsItem(Icons.Default.CloudUpload, "Backup Database") {
            Toast.makeText(context, "Data backup started...", Toast.LENGTH_SHORT).show()
        }

        SettingsItem(Icons.Default.FileDownload, "Export Data") {
            Toast.makeText(context, "Exporting to Excel...", Toast.LENGTH_SHORT).show()
        }

        SettingsItem(Icons.Default.Info, "About Us") {
            // Logic to show app version info
            Toast.makeText(context, "Mahila Shakti Unnati v1.0", Toast.LENGTH_LONG).show()
        }

        Spacer(Modifier.height(20.dp))

        // LOGOUT
        ListItem(
            headlineContent = { Text("Logout", color = Color.Red) },
            leadingContent = { Icon(Icons.Default.Logout, null, tint = Color.Red) },
            modifier = Modifier.clickable {
                // Clear session and go back to login
                val session = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
                session.edit().clear().apply()

                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
                activity.finish()
            }
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = { Icon(icon, null, tint = Color(0xFF6A1B9A)) },
        trailingContent = { Icon(Icons.Default.ChevronRight, null) },
        modifier = Modifier.clickable { onClick() }
    )
    HorizontalDivider(thickness = 0.5.dp)
}