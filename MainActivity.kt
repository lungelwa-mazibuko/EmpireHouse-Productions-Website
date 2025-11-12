package com.example.empirehouseproductions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.empirehouseproductions.presentation.navigation.AppNavigation
import com.example.empirehouseproductions.presentation.theme.EmpireHouseProductionsTheme
import com.example.empirehouseproductions.util.FirebaseInitializer
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize sample data in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseInitializer().initializeSampleData()
            } catch (e: Exception) {
                println("Error initializing sample data: ${e.message}")
            }
        }

        setContent {
            EmpireHouseProductionsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material.MaterialTheme.colors.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}