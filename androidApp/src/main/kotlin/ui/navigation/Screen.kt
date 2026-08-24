package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object ServiceDetail : Screen("service_detail/{serviceId}") {
        fun createRoute(serviceId: String) = "service_detail/$serviceId"
    }
    object Booking : Screen("booking/{serviceId}") {
        fun createRoute(serviceId: String) = "booking/$serviceId"
    }
    object SelectAddress : Screen("select_address/{serviceId}") {
        fun createRoute(serviceId: String) = "select_address/$serviceId"
    }
    object ConfirmBooking : Screen("confirm_booking/{serviceId}") {
        fun createRoute(serviceId: String) = "confirm_booking/$serviceId"
    }
    object Payment : Screen("payment/{serviceId}") {
        fun createRoute(serviceId: String) = "payment/$serviceId"
    }
    object PaymentSuccess : Screen("payment_success/{appointmentId}") {
        fun createRoute(appointmentId: String) = "payment_success/$appointmentId"
    }
    object PaymentCheckout : Screen("payment_checkout/{appointmentId}") {
        fun createRoute(appointmentId: String) = "payment_checkout/$appointmentId"
    }
    object Appointments : Screen("appointments")
    object AppointmentDetail : Screen("appointment_detail/{appointmentId}") {
        fun createRoute(appointmentId: String) = "appointment_detail/$appointmentId"
    }
    object NurseDetail : Screen("nurse_detail/{nurseId}?serviceId={serviceId}") {
        fun createRoute(nurseId: String, serviceId: String = "NURSING_1") = "nurse_detail/$nurseId?serviceId=$serviceId"
    }
    object NurseTracking : Screen("nurse_tracking/{appointmentId}") {
        fun createRoute(appointmentId: String) = "nurse_tracking/$appointmentId"
    }
    object Profile : Screen("profile")
    object Support : Screen("support")
    object CareAdvisorAI : Screen("care_advisor_ai")
    object AllServices : Screen("all_services")
    object Notifications : Screen("notifications")
    object AllNotifications : Screen("all_notifications")
}
