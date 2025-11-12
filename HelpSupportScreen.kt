package com.example.empirehouseproduction.presentation.screens.support

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
fun HelpSupportScreen(
    onBackClick: () -> Unit,
    onContactSupport: () -> Unit,
    onFaqClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
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
            // Quick Help Section
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
                    Icon(
                        Icons.Default.Help,
                        contentDescription = "Help",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "How can we help you?",
                        style = MaterialTheme.typography.h2,
                        fontSize = 24.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Find answers to common questions or contact our support team",
                        style = MaterialTheme.typography.body1,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                    )
                }
            }

            // Quick Actions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Quick Help",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SupportActionItem(
                        title = "FAQs",
                        subtitle = "Frequently asked questions",
                        icon = Icons.Default.QuestionAnswer,
                        onClick = onFaqClick
                    )
                    
                    SupportActionItem(
                        title = "Booking Guide",
                        subtitle = "How to book studio sessions",
                        icon = Icons.Default.CalendarToday,
                        onClick = { /* TODO */ }
                    )
                    
                    SupportActionItem(
                        title = "Equipment Guide",
                        subtitle = "Using studio equipment",
                        icon = Icons.Default.CameraAlt,
                        onClick = { /* TODO */ }
                    )
                    
                    SupportActionItem(
                        title = "Payment Help",
                        subtitle = "Payment methods and issues",
                        icon = Icons.Default.Payment,
                        onClick = { /* TODO */ }
                    )
                }
            }

            // Contact Options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Contact Support",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ContactOptionItem(
                        title = "Live Chat",
                        subtitle = "Chat with our support team",
                        icon = Icons.Default.Chat,
                        available = true,
                        onClick = onContactSupport
                    )
                    
                    ContactOptionItem(
                        title = "Email Support",
                        subtitle = "support@empirehouse.com",
                        icon = Icons.Default.Email,
                        available = true,
                        onClick = { /* TODO */ }
                    )
                    
                    ContactOptionItem(
                        title = "Phone Support",
                        subtitle = "+1 (555) 123-4567",
                        icon = Icons.Default.Phone,
                        available = true,
                        onClick = { /* TODO */ }
                    )
                    
                    ContactOptionItem(
                        title = "Visit Studio",
                        subtitle = "123 Studio Street, City",
                        icon = Icons.Default.LocationOn,
                        available = true,
                        onClick = { /* TODO */ }
                    )
                }
            }

            // Support Hours
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Support Hours",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SupportHourItem("Monday - Friday", "9:00 AM - 8:00 PM")
                    SupportHourItem("Saturday", "10:00 AM - 6:00 PM") 
                    SupportHourItem("Sunday", "12:00 PM - 5:00 PM")
                    SupportHourItem("Emergency Support", "24/7 for urgent issues")
                }
            }

            // Emergency Contact
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = "Emergency", tint = MaterialTheme.colors.error)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Emergency Contact",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "For urgent studio equipment issues or safety concerns:",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "📞 +1 (555) 911-HELP",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SupportActionItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.body1)
                Text(subtitle, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun ContactOptionItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    available: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp,
        onClick = onClick,
        backgroundColor = if (available) MaterialTheme.colors.surface 
                         else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = if (available) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.body1, color = if (available) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                Text(subtitle, style = MaterialTheme.typography.caption, color = if (available) MaterialTheme.colors.onSurface.copy(alpha = 0.7f) else MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
            }
            if (available) {
                Surface(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "Available",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Surface(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "Offline",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SupportHourItem(day: String, hours: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day, style = MaterialTheme.typography.body1)
        Text(hours, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium, color = MaterialTheme.colors.primary)
    }
}