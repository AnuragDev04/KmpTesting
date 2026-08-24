package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// --- Service Catalog Model ---
data class HealthcareService(
    val id: String,
    val name: String,
    val category: ServiceCategory,
    val price: Double,
    val priceUnit: String, // e.g. "per visit", "12 hrs shift", "24 hrs care"
    val duration: String,
    val description: String,
    val inclusions: List<String>,
    val imageDrawableRes: String? = null,
    val isPopular: Boolean = false,
    val rating: Double = 4.9,
    val reviewsCount: Int = 120
)

enum class ServiceCategory(val displayName: String, val iconName: String) {
    HOME_NURSING("Home Nursing", "Medical Services"),
    VACCINATION("Vaccination", "Vaccination"),
    PATIENT_CARE("Patient Care", "Personal Nursing"),
    ELDER_CARE("Elder Care", "Elderly Assistance"),
    MEDICAL_ASSISTANCE("Medical Assistance", "Diagnostics & Vitals"),
    PHYSIOTHERAPY("Physiotherapy", "Physical Therapy"),
    LAB_TEST("Lab Test", "Laboratory"),
    EMERGENCY_CARE("Emergency Care", "SOS Critical Care"),
    HOSPITALITY("Hospice Care", "Recovery Care")
}

// --- Nurse Profile Model ---
data class Nurse(
    val id: String,
    val name: String,
    val profession: String = "Staff Nurse",
    val qualification: String, // e.g., "B.Sc Nursing (ICU Certified)"
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val patientsServed: String = "320+",
    val satisfactionPercent: Int = 98,
    val phone: String,
    val specialization: String,
    val isPoliceVerified: Boolean = true,
    val isAvailable: Boolean = true,
    val bio: String,
    val languages: List<String> = listOf("English", "Hindi", "Kannada"),
    val completedVisits: Int = 240,
    val imageDrawableResName: String? = null,
    val isFavorite: Boolean = false
)

// --- Room Database Entities ---

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val phone: String,
    val name: String,
    val email: String,
    val address: String,
    val pincode: String,
    val emergencyContact: String,
    val bloodGroup: String,
    val medicalNotes: String,
    val isLoggedIn: Boolean = true
)

enum class AppointmentStatus(val displayName: String) {
    CONFIRMED("Confirmed"),
    PENDING("Pending"),
    NURSE_ASSIGNED("Nurse Assigned"),
    EN_ROUTE("Nurse En Route"),
    IN_PROGRESS("Care In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val serviceId: String,
    val serviceName: String,
    val categoryName: String,
    val price: Double,
    val date: String,
    val timeSlot: String,
    val address: String,
    val patientName: String,
    val patientAge: Int,
    val specialNotes: String,
    val paymentGateway: String, // e.g. "Razorpay", "PhonePe", "Stripe"
    val paymentMethod: String, // e.g. "UPI", "Credit Card", "Net Banking"
    val paymentStatus: String, // "SUCCESS", "PENDING", "FAILED"
    val transactionId: String,
    val invoiceNumber: String,
    val status: String, // AppointmentStatus name
    val assignedNurseId: String? = null,
    val assignedNurseName: String? = null,
    val assignedNursePhone: String? = null,
    val assignedNurseQualification: String? = null,
    val assignedNurseRating: Double? = null,
    val ratingGiven: Float? = null,
    val reviewGiven: String? = null,
    val cancellationReason: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userPhone: String,
    val subject: String,
    val message: String,
    val status: String = "OPEN", // "OPEN", "IN_PROGRESS", "RESOLVED"
    val timestamp: Long = System.currentTimeMillis()
)

// --- Notification System Models ---
enum class NotificationType {
    APPOINTMENT_REMINDER,
    PAYMENT_SUCCESS,
    NURSE_ASSIGNED,
    SPECIAL_OFFER
}

data class HealthcareNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: String,
    val isRead: Boolean = false,
    val relatedAppointmentId: String? = null,
    val relatedServiceId: String? = null,
    val relatedNurseId: String? = null
)
