package com.example.empirehouseproduction.presentation.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // App Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                "EH",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Empire House Productions",
                        style = MaterialTheme.typography.h2,
                        fontSize = 24.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        "Studio Booking System",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Version 1.0.0 (Build 1001)",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // App Description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "About Empire House",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Empire House Productions is a premier studio booking platform designed for professional filmmakers, photographers, and content creators. " +
                        "Our state-of-the-art facilities and cutting-edge equipment provide the perfect environment for bringing creative visions to life.",
                        style = MaterialTheme.typography.body1,
                        lineHeight = androidx.compose.ui.unit.sp(20)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "With our intuitive booking system, you can easily reserve studio space, manage equipment rentals, and coordinate your production schedule " +
                        "all in one place.",
                        style = MaterialTheme.typography.body1,
                        lineHeight = androidx.compose.ui.unit.sp(20)
                    )
                }
            }

            // Features
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "App Features",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    FeatureItem("Studio Booking", "Book our professional studios with real-time availability")
                    FeatureItem("Equipment Management", "Rent high-end cameras, lighting, and audio equipment")
                    FeatureItem("Payment Processing", "Secure payment system with multiple payment methods")
                    FeatureItem("Role-based Access", "Different experiences for clients, staff, and administrators")
                    FeatureItem("Real-time Updates", "Live updates for bookings and equipment status")
                    FeatureItem("Analytics & Reports", "Comprehensive reporting and analytics dashboard")
                }
            }

            // Technical Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Technical Information",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    InfoItem("Platform", "Android")
                    InfoItem("Minimum SDK", "API 21 (Android 5.0)")
                    InfoItem("Built With", "Kotlin, Jetpack Compose")
                    InfoItem("Backend", "Firebase Firestore")
                    InfoItem("Authentication", "Firebase Auth")
                    InfoItem("Database", "Room DB (Offline Support)")
                    InfoItem("Architecture", "MVVM with Repository Pattern")
                }
            }

            // Team & Credits
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Development Team",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TeamMember("Lead Developer", "Your Name", "your.email@empirehouse.com")
                    TeamMember("UI/UX Designer", "Design Team", "design@empirehouse.com")
                    TeamMember("Project Manager", "Management Team", "management@empirehouse.com")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "Special Thanks",
                        style = MaterialTheme.typography.h2,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Thanks to all our beta testers and the amazing developer community for their support and feedback.",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            // Legal Links
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Legal",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LegalLink("Privacy Policy", "https://empirehouse.com/privacy")
                    LegalLink("Terms of Service", "https://empirehouse.com/terms")
                    LegalLink("License Agreement", "https://empirehouse.com/license")
                    LegalLink("Third-Party Licenses", "https://empirehouse.com/third-party")
                }
            }

            // Copyright
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 2.dp,
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "© 2024 Empire House Productions",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        "All rights reserved",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FeatureItem(feature: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = "Feature",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(feature, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
            Text(description, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TeamMember(role: String, name: String, email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(name, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
        Text(role, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.primary)
        Text(email, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun LegalLink(title: String, url: String) {
    TextButton(
        onClick = { /* TODO: Open web view or browser */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, style = MaterialTheme.typography.body1)
    }
}