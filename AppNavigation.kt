package com.example.empirehouseproductions.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.empirehouseproductions.presentation.screens.admin.AdminDashboard
import com.example.empirehouseproductions.presentation.screens.admin.AnalyticsScreen
import com.example.empirehouseproductions.presentation.screens.admin.UserManagementScreen
import com.example.empirehouseproductions.presentation.screens.admin.SystemConfigScreen
import com.example.empirehouseproductions.presentation.screens.auth.LoginScreen
import com.example.empirehouseproductions.presentation.screens.auth.RegisterScreen
import com.example.empirehouseproductions.presentation.screens.booking.BookingListScreen
import com.example.empirehouseproductions.presentation.screens.booking.CreateBookingScreen
import com.example.empirehouseproductions.presentation.screens.clients.ClientsScreen
import com.example.empirehouseproductions.presentation.screens.dashboard.HomeScreen
import com.example.empirehouseproductions.presentation.screens.dashboard.rolebased.ClientDashboard
import com.example.empirehouseproductions.presentation.screens.dashboard.rolebased.StaffDashboard
import com.example.empirehouseproductions.presentation.screens.equipment.EquipmentScreen
import com.example.empirehouseproductions.presentation.screens.payment.PaymentScreen
import com.example.empirehouseproductions.presentation.screens.profile.ProfileScreen
import com.example.empirehouseproductions.presentation.screens.profile.EditProfileScreen
import com.example.empirehouseproductions.presentation.screens.profile.ChangePasswordScreen
import com.example.empirehouseproductions.presentation.screens.reports.ReportsScreen
import com.example.empirehouseproductions.presentation.screens.settings.SettingsScreen
import com.example.empirehouseproductions.presentation.screens.support.HelpSupportScreen
import com.example.empirehouseproductions.presentation.screens.about.AboutScreen
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.sampleBookings
import com.example.empirehouseproductions.presentation.screens.auth.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Remember the navigation state
    val navigationState = remember(navController) {
        NavigationState(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        // Authentication Screens
        composable(Screens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screens.Register.route) }
            )
        }

        composable(Screens.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screens.Login.route) }
            )
        }

        // Main Dashboard
        composable(Screens.Home.route) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screens.Login.route) {
                        popUpTo(Screens.Home.route) { inclusive = true }
                    }
                },
                onBookingsClick = { navController.navigate(Screens.Bookings.route) },
                onCreateBookingClick = { navController.navigate(Screens.CreateBooking.route) },
                onEquipmentClick = { navController.navigate(Screens.Equipment.route) },
                onProfileClick = { navController.navigate(Screens.Profile.route) },
                onSettingsClick = { navController.navigate(Screens.Settings.route) },
                onUsersClick = {
                    // For admin users, navigate to admin dashboard
                    navController.navigate(Screens.AdminDashboard.route)
                },
                onAnalyticsClick = {
                    navController.navigate(Screens.Analytics.route)
                },
                onReportsClick = {
                    navController.navigate(Screens.Reports.route)
                },
                onClientsClick = {
                    navController.navigate(Screens.Clients.route)
                },
                onSystemConfig = {
                    navController.navigate(Screens.SystemConfig.route)
                },
                onHelpSupport = {
                    navController.navigate(Screens.HelpSupport.route)
                },
                onAbout = {
                    navController.navigate(Screens.About.route)
                }
            )
        }

        // Booking Management
        composable(Screens.Bookings.route) {
            BookingListScreen(
                onBackClick = { navController.popBackStack() },
                onCreateBooking = { navController.navigate(Screens.CreateBooking.route) },
                onBookingClick = { bookingId ->
                    // Navigate to booking details or payment
                    navigationState.navigateToBookingDetails(bookingId)
                }
            )
        }

        composable(Screens.CreateBooking.route) {
            CreateBookingScreen(
                onBackClick = { navController.popBackStack() },
                onBookingCreated = { booking ->
                    // Navigate to payment screen with the created booking
                    navigationState.navigateToPayment(booking.id)
                }
            )
        }

        // Equipment Management
        composable(Screens.Equipment.route) {
            EquipmentScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Profile Management
        composable(Screens.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Screens.EditProfile.route) },
                onChangePassword = { navController.navigate(Screens.ChangePassword.route) },
                onSettingsClick = { navController.navigate(Screens.Settings.route) }
            )
        }

        composable(Screens.EditProfile.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onProfileUpdated = {
                    navController.popBackStack()
                    // Optional: Show success message in profile screen
                }
            )
        }

        composable(Screens.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordChanged = {
                    navController.popBackStack()
                    // Optional: Show success message in profile screen
                }
            )
        }

        // Settings & Support
        composable(Screens.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Screens.EditProfile.route) },
                onChangePassword = { navController.navigate(Screens.ChangePassword.route) },
                onNotificationSettings = {
                    // TODO: Navigate to notification settings screen
                    // For now, show a placeholder
                },
                onHelpSupport = { navController.navigate(Screens.HelpSupport.route) },
                onAbout = { navController.navigate(Screens.About.route) }
            )
        }

        composable(Screens.HelpSupport.route) {
            HelpSupportScreen(
                onBackClick = { navController.popBackStack() },
                onContactSupport = {
                    // TODO: Implement contact support
                },
                onFaqClick = {
                    // TODO: Navigate to FAQ screen
                }
            )
        }

        composable(Screens.About.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Admin Screens
        composable(Screens.AdminDashboard.route) {
            AdminDashboard(
                onUsersClick = { navController.navigate(Screens.UserManagement.route) },
                onBookingsClick = { navController.navigate(Screens.Bookings.route) },
                onEquipmentClick = { navController.navigate(Screens.Equipment.route) },
                onAnalyticsClick = { navController.navigate(Screens.Analytics.route) },
                onReportsClick = { navController.navigate(Screens.Reports.route) },
                onSettingsClick = { navController.navigate(Screens.Settings.route) },
                onSystemConfig = { navController.navigate(Screens.SystemConfig.route) },
                onHelpSupport = { navController.navigate(Screens.HelpSupport.route) },
                onAbout = { navController.navigate(Screens.About.route) }
            )
        }

        composable(Screens.UserManagement.route) {
            UserManagementScreen(
                onBackClick = { navController.popBackStack() },
                onUserClick = { userId ->
                    navigationState.navigateToUserDetails(userId)
                }
            )
        }

        composable(Screens.Analytics.route) {
            AnalyticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screens.SystemConfig.route) {
            SystemConfigScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Staff Screens
        composable(Screens.Reports.route) {
            ReportsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screens.Clients.route) {
            ClientsScreen(
                onBackClick = { navController.popBackStack() },
                onClientClick = { clientId ->
                    navigationState.navigateToClientDetails(clientId)
                }
            )
        }

        // Payment Screen with arguments
        composable(
            route = "${Screens.Payment.route}/{bookingId}",
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""

            // Find the booking by ID or use a placeholder
            val booking = sampleBookings.find { it.id == bookingId } ?: sampleBookings.first()

            PaymentScreen(
                booking = booking,
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = {
                    // Navigate to success screen or back to bookings
                    navController.popBackStack(Screens.Bookings.route, inclusive = false)
                    // TODO: Show success message
                },
                onPaymentFailed = { errorMessage ->
                    // Show error message (could use snackbar)
                    // Stay on payment screen to retry
                }
            )
        }

        // Role-specific dashboards (alternative navigation)
        composable(Screens.ClientDashboard.route) {
            ClientDashboard(
                onBookingsClick = { navController.navigate(Screens.Bookings.route) },
                onCreateBookingClick = { navController.navigate(Screens.CreateBooking.route) },
                onEquipmentClick = { navController.navigate(Screens.Equipment.route) },
                onProfileClick = { navController.navigate(Screens.Profile.route) },
                onSettingsClick = { navController.navigate(Screens.Settings.route) },
                onHelpSupport = { navController.navigate(Screens.HelpSupport.route) }
            )
        }

        composable(Screens.StaffDashboard.route) {
            StaffDashboard(
                onBookingsClick = { navController.navigate(Screens.Bookings.route) },
                onCreateBookingClick = { navController.navigate(Screens.CreateBooking.route) },
                onEquipmentClick = { navController.navigate(Screens.Equipment.route) },
                onReportsClick = { navController.navigate(Screens.Reports.route) },
                onClientsClick = { navController.navigate(Screens.Clients.route) },
                onSettingsClick = { navController.navigate(Screens.Settings.route) },
                onHelpSupport = { navController.navigate(Screens.HelpSupport.route) }
            )
        }

        // Detail screens with arguments (placeholder implementations)
        composable(
            route = "${Screens.Bookings.route}/{bookingId}",
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            // TODO: Implement Booking Details Screen
            BookingDetailsPlaceholder(
                bookingId = bookingId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screens.Clients.route}/{clientId}",
            arguments = listOf(
                navArgument("clientId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
            // TODO: Implement Client Details Screen
            ClientDetailsPlaceholder(
                clientId = clientId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screens.UserManagement.route}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            // TODO: Implement User Details Screen
            UserDetailsPlaceholder(
                userId = userId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }

    // Handle deep links or initial navigation based on auth state
    LaunchedEffect(authViewModel.isUserLoggedIn) {
        if (authViewModel.isUserLoggedIn.value) {
            // User is logged in, ensure we're on home screen
            if (navController.currentDestination?.route != Screens.Home.route) {
                navController.navigate(Screens.Home.route) {
                    popUpTo(0) // Clear back stack
                }
            }
        } else {
            // User is not logged in, ensure we're on login screen
            if (navController.currentDestination?.route != Screens.Login.route) {
                navController.navigate(Screens.Login.route) {
                    popUpTo(0) // Clear back stack
                }
            }
        }
    }
}

// Navigation state class to manage navigation logic
class NavigationState(private val navController: NavHostController) {

    fun navigateToBookingDetails(bookingId: String) {
        navController.navigate("${Screens.Bookings.route}/$bookingId")
    }

    fun navigateToPayment(bookingId: String) {
        navController.navigate("${Screens.Payment.route}/$bookingId")
    }

    fun navigateToClientDetails(clientId: String) {
        navController.navigate("${Screens.Clients.route}/$clientId")
    }

    fun navigateToUserDetails(userId: String) {
        navController.navigate("${Screens.UserManagement.route}/$userId")
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToRoute(route: String) {
        navController.navigate(route) {
            // Prevent multiple copies of the same destination
            launchSingleTop = true
        }
    }

    fun clearBackStackAndNavigate(route: String) {
        navController.navigate(route) {
            popUpTo(0) // Clear entire back stack
        }
    }

    fun navigateToHome() {
        clearBackStackAndNavigate(Screens.Home.route)
    }

    fun navigateToLogin() {
        clearBackStackAndNavigate(Screens.Login.route)
    }

    fun navigateToSettings() {
        navigateToRoute(Screens.Settings.route)
    }

    fun navigateToProfile() {
        navigateToRoute(Screens.Profile.route)
    }

    fun navigateToHelpSupport() {
        navigateToRoute(Screens.HelpSupport.route)
    }

    fun navigateToAbout() {
        navigateToRoute(Screens.About.route)
    }
}

// Extension function for easy navigation
fun NavHostController.navigateToBookingPayment(booking: Booking) {
    this.navigate("${Screens.Payment.route}/${booking.id}")
}

// Placeholder composables for detail screens (to be implemented)
@Composable
fun BookingDetailsPlaceholder(bookingId: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Booking Details for: $bookingId\n\nThis screen will show detailed booking information, client details, equipment used, and booking status.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ClientDetailsPlaceholder(clientId: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Client Details for: $clientId\n\nThis screen will show comprehensive client information, booking history, preferences, and contact details.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun UserDetailsPlaceholder(userId: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "User Details for: $userId\n\nThis screen will show user profile, role information, activity history, and management options.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}