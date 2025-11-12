package com.example.empirehouseproductions.presentation.navigation

sealed class Screens(val route: String) {
    // Authentication
    object Login : Screens("login")
    object Register : Screens("register")

    // Main Navigation
    object Home : Screens("home")
    object Profile : Screens("profile")
    object Settings : Screens("settings")

    // Profile Management
    object EditProfile : Screens("edit_profile")
    object ChangePassword : Screens("change_password")

    // Booking Management
    object Bookings : Screens("bookings")
    object CreateBooking : Screens("create_booking")
    object Payment : Screens("payment")

    // Equipment Management
    object Equipment : Screens("equipment")

    // Admin Screens
    object AdminDashboard : Screens("admin_dashboard")
    object UserManagement : Screens("user_management")
    object Analytics : Screens("analytics")
    object SystemConfig : Screens("system_config")

    // Staff Screens
    object Reports : Screens("reports")
    object Clients : Screens("clients")

    // Support & Information
    object HelpSupport : Screens("help_support")
    object About : Screens("about")

    // Role-specific Dashboards
    object ClientDashboard : Screens("client_dashboard")
    object StaffDashboard : Screens("staff_dashboard")

    // Utility function to create route with arguments
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    // Function to create route with query parameters
    fun withParams(vararg params: Pair<String, String>): String {
        return if (params.isEmpty()) {
            route
        } else {
            buildString {
                append(route)
                append("?")
                params.forEachIndexed { index, (key, value) ->
                    if (index > 0) append("&")
                    append("$key=$value")
                }
            }
        }
    }
}

// Extension for string routes to check if it matches a screen
fun String.isScreenRoute(screen: Screens): Boolean {
    return this.startsWith(screen.route)
}

// Navigation arguments
object NavArgs {
    const val BOOKING_ID = "bookingId"
    const val USER_ID = "userId"
    const val CLIENT_ID = "clientId"
    const val EQUIPMENT_ID = "equipmentId"
    const val PAYMENT_ID = "paymentId"
    const val REPORT_TYPE = "reportType"
    const val DATE_RANGE = "dateRange"
}

// Route patterns for deep linking
object DeepLinkRoutes {
    const val BOOKING_DETAILS = "empirehouse://booking/{bookingId}"
    const val PAYMENT = "empirehouse://payment/{bookingId}"
    const val PROFILE = "empirehouse://profile"
    const val SETTINGS = "empirehouse://settings"
    const val HELP = "empirehouse://help"
    const val ABOUT = "empirehouse://about"
}

// Navigation helper functions
fun getRouteWithBookingId(route: String, bookingId: String): String {
    return "$route/$bookingId"
}

fun getRouteWithUserId(route: String, userId: String): String {
    return "$route/$userId"
}

fun getRouteWithClientId(route: String, clientId: String): String {
    return "$route/$clientId"
}