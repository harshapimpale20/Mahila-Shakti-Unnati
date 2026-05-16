package com.example.mahilashaktiunnati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

class SavingHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // MaterialTheme ensures consistent styling and prevents UI-related crashes
            MaterialTheme {
                // Fixed: Added the missing onBack lambda parameter to match SavingHistoryScreen.kt
                SavingHistoryScreen(
                    onBack = {
                        // finish() closes the current activity and returns to the Dashboard
                        finish()
                    }
                )
            }
        }
    }
}