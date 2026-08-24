package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiCareAdvisor
import com.example.data.local.AppDatabase
import com.example.data.models.*
import com.example.data.repository.HealthcareRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = HealthcareRepository(db.userDao(), db.appointmentDao(), db.supportTicketDao())

    val loggedInUser: StateFlow<UserEntity?> = repository.loggedInUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allAppointments: StateFlow<List<AppointmentEntity>> = repository.allAppointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSupportTickets: StateFlow<List<SupportTicketEntity>> = repository.allSupportTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Services Catalog State ---
    val services: List<HealthcareService> = repository.getAvailableServices()
    val nurses: List<Nurse> = repository.getAvailableNurses()

    private val _selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val selectedCategory: StateFlow<ServiceCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun selectCategory(category: ServiceCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredServices: List<HealthcareService>
        get() {
            val query = _searchQuery.value.trim().lowercase()
            val cat = _selectedCategory.value
            return services.filter { service ->
                val matchesCat = (cat == null || service.category == cat)
                val matchesQuery = query.isEmpty() ||
                        service.name.lowercase().contains(query) ||
                        service.description.lowercase().contains(query) ||
                        service.category.displayName.lowercase().contains(query)
                matchesCat && matchesQuery
            }
        }

    // --- Booking Form Draft State ---
    var bookingDate = MutableStateFlow(getTomorrowDate())
    var bookingTimeSlot = MutableStateFlow("10:00 AM - 12:00 PM")
    var bookingPatientName = MutableStateFlow("")
    var bookingPatientAge = MutableStateFlow("65")
    var bookingAddress = MutableStateFlow("")
    var bookingPincode = MutableStateFlow("201301")
    var bookingSpecialNotes = MutableStateFlow("")

    // --- Payment Checkout State ---
    var selectedPaymentGateway = MutableStateFlow("Razorpay") // Razorpay, Stripe, PhonePe, PayU
    var selectedPaymentMethod = MutableStateFlow("UPI") // UPI, Card, NetBanking, Wallet
    var couponCode = MutableStateFlow("CARE10") // 10% discount coupon
    var isCouponApplied = MutableStateFlow(true)
    var isPaymentProcessing = MutableStateFlow(false)
    var paymentSuccessAppointmentId = MutableStateFlow<String?>(null)

    // --- AI Care Advisor State ---
    var aiQuery = MutableStateFlow("")
    var aiPatientAge = MutableStateFlow("65")
    var aiCondition = MutableStateFlow("Post surgery wound care & high blood pressure")
    var aiResponse = MutableStateFlow<String?>(null)
    var isAiLoading = MutableStateFlow(false)

    // --- Dynamic Notification System ---
    private val _notifications = MutableStateFlow<List<HealthcareNotification>>(
        listOf(
            HealthcareNotification(
                id = "NOTIF_1",
                title = "Appointment Reminder",
                message = "Your Home Nursing appointment is tomorrow at 10:00 AM",
                type = NotificationType.APPOINTMENT_REMINDER,
                timestamp = "10:00 AM",
                isRead = false,
                relatedAppointmentId = "BMJ1254789"
            ),
            HealthcareNotification(
                id = "NOTIF_2",
                title = "Payment Successful",
                message = "Payment of ₹739 received successfully.",
                type = NotificationType.PAYMENT_SUCCESS,
                timestamp = "Yesterday",
                isRead = false,
                relatedAppointmentId = "BMJ1254789"
            ),
            HealthcareNotification(
                id = "NOTIF_3",
                title = "Nurse Assigned",
                message = "Priya Sharma has been assigned to your appointment.",
                type = NotificationType.NURSE_ASSIGNED,
                timestamp = "18 May",
                isRead = false,
                relatedNurseId = "NURSE_101",
                relatedAppointmentId = "BMJ1254789"
            ),
            HealthcareNotification(
                id = "NOTIF_4",
                title = "Special Offer",
                message = "Get 20% OFF on Lab Tests. Book now!",
                type = NotificationType.SPECIAL_OFFER,
                timestamp = "16 May",
                isRead = false,
                relatedServiceId = "LAB_1"
            )
        )
    )
    val notifications: StateFlow<List<HealthcareNotification>> = _notifications.asStateFlow()

    val unreadNotificationsCount: StateFlow<Int> = _notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 4)

    fun markNotificationAsRead(notificationId: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    fun addNotification(notification: HealthcareNotification) {
        _notifications.value = listOf(notification) + _notifications.value
    }

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNecessary()
        }
    }

    fun applyCoupon(code: String): Boolean {
        return if (code.trim().uppercase() == "CARE10" || code.trim().uppercase() == "HEALTH20") {
            couponCode.value = code.trim().uppercase()
            isCouponApplied.value = true
            true
        } else {
            false
        }
    }

    fun removeCoupon() {
        isCouponApplied.value = false
        couponCode.value = ""
    }

    // --- Process Booking & Trigger Payment Gateway ---
    fun processBookingAndPayment(
        service: HealthcareService,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            isPaymentProcessing.value = true
            delay(1500) // Simulate gateway security handshakes & OTP verification

            val user = loggedInUser.value
            val patientName = bookingPatientName.value.ifBlank { user?.name ?: "Patient" }
            val age = bookingPatientAge.value.toIntOrNull() ?: 60
            val addr = bookingAddress.value.ifBlank { user?.address ?: "Home Address" }
            val notes = bookingSpecialNotes.value

            val apptId = "BMJ" + (1000000..9999999).random()
            val txnId = "TXN_" + selectedPaymentGateway.value.take(3).uppercase() + "_" + (100000..999999).random()
            val invNum = "INV-2026-" + (1000..9999).random()

            val basePrice = service.price
            val discount = if (isCouponApplied.value) basePrice * 0.10 else 0.0
            val finalPrice = (basePrice - discount) * 1.18 // Including 18% GST

            // Auto allocate best available nurse
            val assignedNurse = nurses.firstOrNull { it.isAvailable } ?: nurses.first()

            val newAppointment = AppointmentEntity(
                id = apptId,
                serviceId = service.id,
                serviceName = service.name,
                categoryName = service.category.displayName,
                price = String.format(Locale.US, "%.2f", finalPrice).toDouble(),
                date = bookingDate.value,
                timeSlot = bookingTimeSlot.value,
                address = addr,
                patientName = patientName,
                patientAge = age,
                specialNotes = notes,
                paymentGateway = selectedPaymentGateway.value,
                paymentMethod = selectedPaymentMethod.value,
                paymentStatus = "SUCCESS",
                transactionId = txnId,
                invoiceNumber = invNum,
                status = AppointmentStatus.NURSE_ASSIGNED.name,
                assignedNurseId = assignedNurse.id,
                assignedNurseName = assignedNurse.name,
                assignedNursePhone = assignedNurse.phone,
                assignedNurseQualification = assignedNurse.qualification,
                assignedNurseRating = assignedNurse.rating
            )

            repository.createAppointment(newAppointment)
            isPaymentProcessing.value = false
            paymentSuccessAppointmentId.value = apptId
            onSuccess(apptId)
        }
    }

    // --- Actions ---
    fun cancelAppointment(id: String, reason: String) {
        viewModelScope.launch {
            repository.cancelAppointment(id, reason)
        }
    }

    fun rescheduleAppointment(id: String, newDate: String, newTimeSlot: String) {
        viewModelScope.launch {
            repository.rescheduleAppointment(id, newDate, newTimeSlot)
        }
    }

    fun submitRating(id: String, rating: Float, review: String) {
        viewModelScope.launch {
            repository.submitRating(id, rating, review)
        }
    }

    fun loginOrRegister(phone: String, name: String, email: String, address: String, emergency: String, blood: String, notes: String) {
        viewModelScope.launch {
            repository.loginOrRegisterUser(
                phone = phone,
                name = name,
                email = email,
                address = address,
                pincode = "201301",
                emergencyContact = emergency,
                bloodGroup = blood,
                medicalNotes = notes
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun updateUserProfile(name: String, email: String, address: String, emergencyContact: String, bloodGroup: String) {
        viewModelScope.launch {
            val currentUser = loggedInUser.value
            if (currentUser != null) {
                repository.loginOrRegisterUser(
                    phone = currentUser.phone,
                    name = name,
                    email = email,
                    address = address,
                    pincode = currentUser.pincode,
                    emergencyContact = emergencyContact,
                    bloodGroup = bloodGroup,
                    medicalNotes = currentUser.medicalNotes
                )
            }
        }
    }

    fun submitSupportTicket(subject: String, message: String, onDone: () -> Unit) {
        viewModelScope.launch {
            val phone = loggedInUser.value?.phone ?: "+91 9876543210"
            repository.submitSupportTicket(phone, subject, message)
            onDone()
        }
    }

    fun askAiCareAdvisor() {
        if (aiQuery.value.isBlank()) return
        viewModelScope.launch {
            isAiLoading.value = true
            val advice = GeminiCareAdvisor.getCareAdvice(
                userQuery = aiQuery.value,
                patientAge = aiPatientAge.value,
                condition = aiCondition.value
            )
            aiResponse.value = advice
            isAiLoading.value = false
        }
    }

    private fun getTomorrowDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val format = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        return format.format(calendar.time)
    }
}
