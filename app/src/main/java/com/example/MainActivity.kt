package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.CareHomeTheme
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CareHomeTheme {
                val navController = rememberNavController()
                val loggedInUser by viewModel.loggedInUser.collectAsState()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val bottomBarRoutes = listOf(
                    Screen.Home.route,
                    Screen.Appointments.route,
                    Screen.Profile.route,
                    Screen.AllServices.route,
                    "services"
                )

                val showBottomBar = currentRoute in bottomBarRoutes

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar && loggedInUser != null) {
                            com.example.ui.components.HealthcareBottomBar(
                                currentRoute = currentRoute,
                                onNavigateHome = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onNavigateAppointments = {
                                    navController.navigate(Screen.Appointments.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onBookNowClick = {
                                    navController.navigate(Screen.Booking.createRoute("NURSING_1"))
                                },
                                onNavigateServices = {
                                    navController.navigate(Screen.AllServices.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onNavigateProfile = {
                                    navController.navigate(Screen.Profile.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    val startDestination = if (loggedInUser != null) Screen.Home.route else Screen.Login.route

                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            AuthScreen(
                                viewModel = viewModel,
                                onAuthSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onServiceClick = { serviceId ->
                                    navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                                },
                                onBookServiceClick = { serviceId ->
                                    navController.navigate(Screen.Booking.createRoute(serviceId))
                                },
                                onViewAllServicesClick = {
                                    navController.navigate(Screen.AllServices.route)
                                },
                                onNurseClick = { nurseId ->
                                    navController.navigate(Screen.NurseDetail.createRoute(nurseId))
                                },
                                onNavigateToNotifications = {
                                    navController.navigate(Screen.Notifications.route)
                                },
                                onNavigateToAiAdvisor = {
                                    navController.navigate(Screen.CareAdvisorAI.route)
                                },
                                onNavigateToAppointments = {
                                    navController.navigate(Screen.Appointments.route)
                                },
                                onNavigateToProfile = {
                                    navController.navigate(Screen.Profile.route)
                                },
                                onNavigateToSupport = {
                                    navController.navigate(Screen.Support.route)
                                }
                            )
                        }

                        composable(Screen.AllServices.route) {
                            AllServicesScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onServiceClick = { serviceId ->
                                    navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                                }
                            )
                        }

                        composable(
                            route = Screen.ServiceDetail.route,
                            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            ServiceDetailScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onBookClick = { id ->
                                    navController.navigate(Screen.Booking.createRoute(id))
                                },
                                onNurseClick = { nurseId ->
                                    navController.navigate(Screen.NurseDetail.createRoute(nurseId, serviceId))
                                }
                            )
                        }

                        composable(
                            route = "nurse_detail/{nurseId}?serviceId={serviceId}",
                            arguments = listOf(
                                navArgument("nurseId") { type = NavType.StringType },
                                navArgument("serviceId") {
                                    type = NavType.StringType
                                    defaultValue = "NURSING_1"
                                }
                            )
                        ) { backStackEntry ->
                            val nurseId = backStackEntry.arguments?.getString("nurseId") ?: "NURSE_101"
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            NurseDetailScreen(
                                nurseId = nurseId,
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onBookWithNurseClick = { sId, _ ->
                                    navController.navigate(Screen.Booking.createRoute(sId))
                                }
                            )
                        }

                        composable(
                            route = Screen.Booking.route,
                            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            BookingScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onProceedToAddress = { id ->
                                    navController.navigate(Screen.SelectAddress.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.SelectAddress.route,
                            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            SelectAddressScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onProceedToConfirm = { id ->
                                    navController.navigate(Screen.ConfirmBooking.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.ConfirmBooking.route,
                            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            ConfirmBookingScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onEditBooking = {
                                    navController.navigate(Screen.Booking.createRoute(serviceId))
                                },
                                onProceedToPayment = { id ->
                                    navController.navigate(Screen.Payment.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.Payment.route,
                            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "NURSING_1"
                            PaymentScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onPaymentSuccess = { newApptId ->
                                    navController.navigate(Screen.PaymentSuccess.createRoute(newApptId)) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.PaymentSuccess.route,
                            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: "BMJ1254789"
                            PaymentSuccessScreen(
                                appointmentId = appointmentId,
                                viewModel = viewModel,
                                onViewAppointment = { apptId ->
                                    navController.navigate(Screen.AppointmentDetail.createRoute(apptId)) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.PaymentCheckout.route,
                            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val serviceId = backStackEntry.arguments?.getString("appointmentId") ?: "NURSING_1"
                            PaymentCheckoutScreen(
                                serviceId = serviceId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onPaymentSuccess = { newApptId ->
                                    navController.navigate(Screen.PaymentSuccess.createRoute(newApptId)) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        composable(Screen.Appointments.route) {
                            AppointmentsListScreen(
                                viewModel = viewModel,
                                onAppointmentClick = { apptId ->
                                    navController.navigate(Screen.AppointmentDetail.createRoute(apptId))
                                },
                                onTrackNurseClick = { apptId ->
                                    navController.navigate(Screen.NurseTracking.createRoute(apptId))
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.AppointmentDetail.route,
                            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val apptId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                            AppointmentDetailScreen(
                                appointmentId = apptId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onTrackNurseClick = { id ->
                                    navController.navigate(Screen.NurseTracking.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.NurseTracking.route,
                            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val apptId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                            NurseTrackingScreen(
                                appointmentId = apptId,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onNavigateToAppointments = { navController.navigate(Screen.Appointments.route) },
                                onNavigateToSupport = { navController.navigate(Screen.Support.route) },
                                onNavigateToAiAdvisor = { navController.navigate(Screen.CareAdvisorAI.route) },
                                onLogoutSuccess = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Support.route) {
                            SupportScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.CareAdvisorAI.route) {
                            CareAdvisorAIScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onBookServiceClick = { serviceId ->
                                    navController.navigate(Screen.Booking.createRoute(serviceId))
                                }
                            )
                        }

                        composable(Screen.Notifications.route) {
                            NotificationsScreen(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() },
                                onAppointmentClick = { apptId ->
                                    navController.navigate(Screen.AppointmentDetail.createRoute(apptId))
                                },
                                onPaymentSuccessClick = { apptId ->
                                    navController.navigate(Screen.PaymentSuccess.createRoute(apptId))
                                },
                                onNurseClick = { nurseId ->
                                    navController.navigate(Screen.NurseDetail.createRoute(nurseId))
                                },
                                onServiceClick = { serviceId ->
                                    navController.navigate(Screen.ServiceDetail.createRoute(serviceId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
