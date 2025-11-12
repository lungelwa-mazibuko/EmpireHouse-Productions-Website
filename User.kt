package com.example.empirehouseproductions.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.CLIENT,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    // Client-specific fields (optional, can be loaded separately)
    val totalBookings: Int = 0,
    val totalSpent: Double = 0.0,
    val lastBookingDate: Long = 0L,
    val averageRating: Double = 0.0,
    val notes: String = "",
    val preferences: UserPreferences = UserPreferences()
) {
    // Computed properties for UI
    val isVIP: Boolean
        get() = totalBookings > 10

    val memberSince: String
        get() = formatDate(createdAt)

    val lastActivity: String
        get() = if (lastBookingDate > 0) formatRelativeTime(lastBookingDate) else "No bookings yet"

    val clientTier: ClientTier
        get() = when {
            totalBookings >= 20 -> ClientTier.PLATINUM
            totalBookings >= 10 -> ClientTier.GOLD
            totalBookings >= 5 -> ClientTier.SILVER
            else -> ClientTier.BRONZE
        }
}

enum class UserRole {
    CLIENT, STAFF, ADMIN
}

enum class ClientTier(val displayName: String, val color: String) {
    BRONZE("Bronze", "#CD7F32"),
    SILVER("Silver", "#C0C0C0"),
    GOLD("Gold", "#FFD700"),
    PLATINUM("Platinum", "#E5E4E2")
}

data class UserPreferences(
    val preferredStudio: String = "",
    val preferredEquipment: List<String> = emptyList(),
    val notificationEnabled: Boolean = true,
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = false,
    val marketingEmails: Boolean = false
)

// Extension functions for User
fun User.getDisplayRole(): String {
    return when (this.role) {
        UserRole.ADMIN -> "Administrator"
        UserRole.STAFF -> "Staff Member"
        UserRole.CLIENT -> "Client"
    }
}

fun User.getRoleColor(): androidx.compose.ui.graphics.Color {
    return when (this.role) {
        UserRole.ADMIN -> androidx.compose.ui.graphics.Color(0xFFD32F2F) // Red
        UserRole.STAFF -> androidx.compose.ui.graphics.Color(0xFF1976D2) // Blue
        UserRole.CLIENT -> androidx.compose.ui.graphics.Color(0xFF388E3C) // Green
    }
}

fun User.getInitials(): String {
    return this.fullName.split(" ")
        .take(2)
        .joinToString("") { it.firstOrNull()?.toString() ?: "" }
        .uppercase()
}

fun User.getAvatarColor(): androidx.compose.ui.graphics.Color {
    val colors = listOf(
        androidx.compose.ui.graphics.Color(0xFFE57373), // Red
        androidx.compose.ui.graphics.Color(0xFF9575CD), // Purple
        androidx.compose.ui.graphics.Color(0xFF4FC3F7), // Light Blue
        androidx.compose.ui.graphics.Color(0xFF81C784), // Green
        androidx.compose.ui.graphics.Color(0xFFFFB74D), // Orange
        androidx.compose.ui.graphics.Color(0xFF4DB6AC), // Teal
        androidx.compose.ui.graphics.Color(0xFFF06292), // Pink
        androidx.compose.ui.graphics.Color(0xFF7986CB)  // Indigo
    )
    val index = this.id.hashCode() % colors.size
    return colors[Math.abs(index)]
}

// Utility functions
private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

private fun formatRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val days = diff / (24 * 60 * 60 * 1000)

    return when {
        days == 0L -> "Today"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        days < 30 -> "${days / 7} weeks ago"
        else -> "${days / 30} months ago"
    }
}

// Sample data for preview and testing
val sampleUsers = listOf(
    User(
        id = "user-1",
        email = "admin@empirehouse.com",
        fullName = "System Administrator",
        phone = "+1234567890",
        role = UserRole.ADMIN,
        createdAt = System.currentTimeMillis() - 86400000 * 365, // 1 year ago
        isActive = true
    ),
    User(
        id = "user-2",
        email = "staff@empirehouse.com",
        fullName = "John Staff Member",
        phone = "+1234567891",
        role = UserRole.STAFF,
        createdAt = System.currentTimeMillis() - 86400000 * 180, // 6 months ago
        isActive = true
    ),
    User(
        id = "user-3",
        email = "client.vip@email.com",
        fullName = "Sarah Professional",
        phone = "+1234567892",
        role = UserRole.CLIENT,
        createdAt = System.currentTimeMillis() - 86400000 * 90, // 3 months ago
        isActive = true,
        totalBookings = 15,
        totalSpent = 2500.0,
        lastBookingDate = System.currentTimeMillis() - 86400000 * 7, // 1 week ago
        averageRating = 4.8,
        notes = "VIP client - prefers Studio A",
        preferences = UserPreferences(
            preferredStudio = "Studio A",
            preferredEquipment = listOf("Canon EOS R5", "LED Panel Kit"),
            notificationEnabled = true,
            emailNotifications = true
        )
    ),
    User(
        id = "user-4",
        email = "new.client@email.com",
        fullName = "Mike Newcomer",
        phone = "+1234567893",
        role = UserRole.CLIENT,
        createdAt = System.currentTimeMillis() - 86400000 * 10, // 10 days ago
        isActive = true,
        totalBookings = 2,
        totalSpent = 400.0,
        lastBookingDate = System.currentTimeMillis() - 86400000 * 3, // 3 days ago
        averageRating = 4.5
    ),
    User(
        id = "user-5",
        email = "inactive.client@email.com",
        fullName = "Emma Inactive",
        phone = "+1234567894",
        role = UserRole.CLIENT,
        createdAt = System.currentTimeMillis() - 86400000 * 200, // 200 days ago
        isActive = false,
        totalBookings = 8,
        totalSpent = 1200.0,
        lastBookingDate = System.currentTimeMillis() - 86400000 * 60, // 2 months ago
        averageRating = 4.2
    )
)