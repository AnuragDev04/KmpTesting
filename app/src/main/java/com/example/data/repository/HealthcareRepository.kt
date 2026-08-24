package com.example.data.repository

import com.example.data.local.AppointmentDao
import com.example.data.local.SupportTicketDao
import com.example.data.local.UserDao
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class HealthcareRepository(
    private val userDao: UserDao,
    private val appointmentDao: AppointmentDao,
    private val supportTicketDao: SupportTicketDao
) {

    val loggedInUser: Flow<UserEntity?> = userDao.getLoggedInUser()
    val allAppointments: Flow<List<AppointmentEntity>> = appointmentDao.getAllAppointments()
    val allSupportTickets: Flow<List<SupportTicketEntity>> = supportTicketDao.getAllTickets()

    // --- Hardcoded Catalog of Services ---
    fun getAvailableServices(): List<HealthcareService> {
        return listOf(
            HealthcareService(
                id = "NURSING_1",
                name = "General Home Nursing Visit",
                category = ServiceCategory.HOME_NURSING,
                price = 499.0,
                priceUnit = "per visit",
                duration = "1 - 2 Hours",
                description = "Professional registered nurse visit to your home for dressing, vital monitoring, injections, and routine medical care.",
                inclusions = listOf(
                    "Vital signs check (BP, Pulse, SpO2, Temp)",
                    "Wound cleaning & sterile dressing",
                    "IV / IM Injection & Drip administration",
                    "Medication review & doctor guidance followup"
                ),
                imageDrawableRes = "img_patient_care_1786014413757",
                isPopular = true,
                rating = 4.9,
                reviewsCount = 342
            ),
            HealthcareService(
                id = "NURSING_2",
                name = "24/7 ICU Trained Home Nurse",
                category = ServiceCategory.HOME_NURSING,
                price = 2499.0,
                priceUnit = "per day (24 hrs shift)",
                duration = "24 Hours Shift",
                description = "Full-time dedicated critical care nurse for post-ICU patients, ventilator support, tracheostomy care, and continuous monitoring.",
                inclusions = listOf(
                    "Tracheostomy & Ventilator care",
                    "Catheterization & Ryle's Tube feeding",
                    "Continuous vital signs chart logging",
                    "Bedridden patient hygiene & turning",
                    "Emergency protocol management"
                ),
                imageDrawableRes = "img_hero_banner_1786014371290",
                isPopular = true,
                rating = 5.0,
                reviewsCount = 188
            ),
            HealthcareService(
                id = "VAC_1",
                name = "Home Vaccination Drive",
                category = ServiceCategory.VACCINATION,
                price = 350.0,
                priceUnit = "per shot + vaccine cost",
                duration = "30 Minutes",
                description = "Safe and sterile doorstep vaccination service for children, adults, and seniors with cold-chain maintenance.",
                inclusions = listOf(
                    "Cold-chain transport of vaccines",
                    "Pre-vaccination health assessment",
                    "Sterile administration by qualified nurse",
                    "15-minute post-vaccination observation"
                ),
                imageDrawableRes = "img_vaccination_1786014400045",
                isPopular = false,
                rating = 4.8,
                reviewsCount = 95
            ),
            HealthcareService(
                id = "ELDER_1",
                name = "Compassionate Elder Care",
                category = ServiceCategory.ELDER_CARE,
                price = 1199.0,
                priceUnit = "per day (12 hrs shift)",
                duration = "12 Hours Shift",
                description = "Dedicated caregiver to assist senior citizens with daily routine, mobility, medication reminders, and warm companionship.",
                inclusions = listOf(
                    "Assistance with bathing & personal hygiene",
                    "Timely medication administration",
                    "Light mobility & walking assistance",
                    "Nutritional meal serving",
                    "Friendly companionship & mental engagement"
                ),
                imageDrawableRes = "img_elder_care_1786014385499",
                isPopular = true,
                rating = 4.9,
                reviewsCount = 260
            ),
            HealthcareService(
                id = "PATIENT_1",
                name = "Post-Surgical Recovery Care",
                category = ServiceCategory.PATIENT_CARE,
                price = 1499.0,
                priceUnit = "per visit (4 hrs)",
                duration = "4 Hours",
                description = "Specialized post-op care focusing on surgical wound healing, drain management, pain relief, and mobility exercises.",
                inclusions = listOf(
                    "Surgical incision inspection & cleaning",
                    "Jackson-Pratt drain / catheter management",
                    "Pain assessment & prescribed medication",
                    "Post-op mobility encouragement"
                ),
                imageDrawableRes = "img_patient_care_1786014413757",
                isPopular = false,
                rating = 4.9,
                reviewsCount = 140
            ),
            HealthcareService(
                id = "MED_1",
                name = "Home Diagnostic Vitals & ECG",
                category = ServiceCategory.MEDICAL_ASSISTANCE,
                price = 699.0,
                priceUnit = "per test session",
                duration = "45 Minutes",
                description = "Diagnostic technician & nurse visit with portable 12-lead ECG, blood glucose monitor, and blood sample collection.",
                inclusions = listOf(
                    "Portable 12-lead ECG recording",
                    "Random Blood Sugar & HbA1c screening",
                    "Blood & Urine lab sample collection",
                    "Digital report delivery within 12 hours"
                ),
                imageDrawableRes = "img_patient_care_1786014413757",
                isPopular = false,
                rating = 4.7,
                reviewsCount = 82
            ),
            HealthcareService(
                id = "PHYSIO_1",
                name = "Home Physiotherapy Session",
                category = ServiceCategory.PHYSIOTHERAPY,
                price = 799.0,
                priceUnit = "per 45 mins session",
                duration = "45 Minutes",
                description = "Customized physical therapy for joint mobilization, stroke rehabilitation, back pain relief, and post-fracture recovery.",
                inclusions = listOf(
                    "Comprehensive posture & joint mobility assessment",
                    "Targeted therapeutic exercises & stretching",
                    "Pain relief electrotherapy / IFT guidance",
                    "Ergonomic home exercise prescription"
                ),
                imageDrawableRes = "img_patient_care_1786014413757",
                isPopular = true,
                rating = 4.9,
                reviewsCount = 175
            ),
            HealthcareService(
                id = "LAB_1",
                name = "Home Lab Test & Blood Collection",
                category = ServiceCategory.LAB_TEST,
                price = 399.0,
                priceUnit = "per sample collection",
                duration = "20 Minutes",
                description = "Safe home blood sample collection by certified phlebotomist with 100% sterile vacuum tubes and fast digital reports.",
                inclusions = listOf(
                    "Sterile vacuum blood & urine collection",
                    "Complete Blood Count (CBC) & Lipid Profile",
                    "Thyroid profile & Diabetes screening",
                    "NABL certified lab report within 6 hours"
                ),
                imageDrawableRes = "img_vaccination_1786014400045",
                isPopular = false,
                rating = 4.8,
                reviewsCount = 210
            ),
            HealthcareService(
                id = "EMERGENCY_1",
                name = "24/7 Emergency Care Nurse",
                category = ServiceCategory.EMERGENCY_CARE,
                price = 999.0,
                priceUnit = "per emergency triage visit",
                duration = "Immediate (Within 25 mins)",
                description = "Rapid response critical care nurse arriving with emergency kit, oxygen cylinder setup, and live doctor video connect.",
                inclusions = listOf(
                    "Immediate triage & vital stabilization",
                    "Oxygen saturation & nebulization support",
                    "Emergency IV cannulation & medication",
                    "Ambulance coordination if required"
                ),
                imageDrawableRes = "img_hero_banner_1786014371290",
                isPopular = true,
                rating = 5.0,
                reviewsCount = 320
            ),
            HealthcareService(
                id = "HOSP_1",
                name = "Palliative & Hospice Care",
                category = ServiceCategory.HOSPITALITY,
                price = 2899.0,
                priceUnit = "per day",
                duration = "24 Hours Shift",
                description = "Empathetic palliative care focusing on pain reduction, comfort, dignity, and family support for advanced illness care.",
                inclusions = listOf(
                    "Comprehensive pain management protocol",
                    "Bedsore prevention mattress & turning care",
                    "Suctioning & airway maintenance",
                    "Emotional & spiritual comfort support"
                ),
                imageDrawableRes = "img_elder_care_1786014385499",
                isPopular = false,
                rating = 5.0,
                reviewsCount = 64
            )
        )
    }

    // --- Available Nurses ---
    fun getAvailableNurses(): List<Nurse> {
        return listOf(
            Nurse(
                id = "NURSE_101",
                name = "Priya Sharma",
                profession = "Staff Nurse",
                qualification = "B.Sc Nursing (ICU & Home Care)",
                experienceYears = 5,
                rating = 4.8,
                reviewsCount = 120,
                patientsServed = "320+",
                satisfactionPercent = 98,
                phone = "+91 98765 43210",
                specialization = "Home Nursing, IV Drips & Elder Care",
                bio = "Compassionate and experienced nurse specialized in elderly and post-surgical care.",
                languages = listOf("English", "Hindi", "Kannada"),
                completedVisits = 320,
                imageDrawableResName = "nurse_priya_hero_1787048302741"
            ),
            Nurse(
                id = "NURSE_102",
                name = "Ankit Verma",
                profession = "Physiotherapy Specialist",
                qualification = "MPT (Orthopedics Specialist)",
                experienceYears = 6,
                rating = 4.8,
                reviewsCount = 185,
                patientsServed = "410+",
                satisfactionPercent = 99,
                phone = "+91 98765 43211",
                specialization = "Joint Mobility & Posture Correction",
                bio = "Specialized in musculoskeletal rehabilitation, joint mobility restoration, and post-operative orthopedic recovery.",
                languages = listOf("English", "Hindi", "Tamil"),
                completedVisits = 410,
                imageDrawableResName = "nurse_ankit_pic_1787046592042"
            ),
            Nurse(
                id = "NURSE_103",
                name = "Neha Reddy",
                profession = "Senior Staff Nurse",
                qualification = "Senior Staff Nurse (Vaccines & Pediatrics)",
                experienceYears = 7,
                rating = 4.9,
                reviewsCount = 210,
                patientsServed = "550+",
                satisfactionPercent = 97,
                phone = "+91 98765 43212",
                specialization = "Vaccinations & Gentle Sample Collection",
                bio = "Expert in gentle pediatric vaccinations, elder wellness checks, and painless diagnostic sample collection.",
                languages = listOf("English", "Hindi", "Telugu"),
                completedVisits = 550,
                imageDrawableResName = "nurse_neha_pic_1787046614205"
            ),
            Nurse(
                id = "NURSE_104",
                name = "Sister Ananya Verma",
                profession = "Geriatric Care Nurse",
                qualification = "Post Basic B.Sc (Geriatric Nursing)",
                experienceYears = 8,
                rating = 4.9,
                reviewsCount = 230,
                patientsServed = "430+",
                satisfactionPercent = 98,
                phone = "+91 98765 43213",
                specialization = "Post-Op Recovery & Tracheostomy",
                bio = "Dedicated critical care nurse with 8 years of experience. Expert in catheterization and elderly medical support.",
                languages = listOf("English", "Hindi", "Bengali"),
                completedVisits = 430,
                imageDrawableResName = "nurse_priya_pic_1787034588602"
            ),
            Nurse(
                id = "NURSE_105",
                name = "Staff Nurse Rajesh Kumar",
                profession = "Trauma Care Specialist",
                qualification = "GNM (Trauma & Wound Specialist)",
                experienceYears = 6,
                rating = 4.8,
                reviewsCount = 154,
                patientsServed = "310+",
                satisfactionPercent = 96,
                phone = "+91 98765 43214",
                specialization = "Trauma Care & Wound Dressing",
                bio = "Empathetic home nurse specializing in sterile wound dressing, IV drip management, and injury rehabilitation.",
                languages = listOf("English", "Hindi", "Marathi"),
                completedVisits = 310,
                imageDrawableResName = "nurse_ankit_pic_1787046592042"
            )
        )
    }

    // --- Seed Initial User and Default Booking if Database is Empty ---
    suspend fun seedInitialDataIfNecessary() {
        val existingUser = userDao.getUserByPhone("+91 9876543210")
        if (existingUser == null) {
            val user = UserEntity(
                phone = "+91 9876543210",
                name = "Riya Sharma",
                email = "riya.sharma@example.com",
                address = "12th Main, Indiranagar, Bangalore",
                pincode = "560038",
                emergencyContact = "+91 9811223344",
                bloodGroup = "O+",
                medicalNotes = "Routine family elder monitoring & home health consultation."
            )
            userDao.insertOrUpdateUser(user)

            // Seed initial appointments matching reference UI
            val appt1 = AppointmentEntity(
                id = "BMJ1254789",
                serviceId = "NURSING_1",
                serviceName = "Home Nursing",
                categoryName = "Home Nursing",
                price = 739.0,
                date = "24 May 2025",
                timeSlot = "10:00 AM",
                address = "12, Park Street, Bangalore",
                patientName = "Riya Sharma",
                patientAge = 28,
                specialNotes = "General post-treatment checkup and vitals assessment.",
                paymentGateway = "Razorpay",
                paymentMethod = "UPI",
                paymentStatus = "SUCCESS",
                transactionId = "TXN_UPI_778129",
                invoiceNumber = "INV-2026-4401",
                status = AppointmentStatus.CONFIRMED.name,
                assignedNurseId = "NURSE_101",
                assignedNurseName = "Priya Sharma",
                assignedNursePhone = "+91 98765 43210",
                assignedNurseQualification = "B.Sc Nursing (Registered)",
                assignedNurseRating = 4.9
            )

            val appt2 = AppointmentEntity(
                id = "BMJ1254790",
                serviceId = "PHYSIO_1",
                serviceName = "Physiotherapy",
                categoryName = "Physiotherapy",
                price = 799.0,
                date = "26 May 2025",
                timeSlot = "04:00 PM",
                address = "12, Park Street, Bangalore",
                patientName = "Riya Sharma",
                patientAge = 28,
                specialNotes = "Shoulder mobility and posture correction exercises.",
                paymentGateway = "Razorpay",
                paymentMethod = "Credit Card",
                paymentStatus = "SUCCESS",
                transactionId = "TXN_CC_982144",
                invoiceNumber = "INV-2026-4402",
                status = AppointmentStatus.CONFIRMED.name,
                assignedNurseId = "NURSE_102",
                assignedNurseName = "Ankit Verma",
                assignedNursePhone = "+91 98765 43211",
                assignedNurseQualification = "MPT (Orthopedics Specialist)",
                assignedNurseRating = 4.8
            )

            val appt3 = AppointmentEntity(
                id = "BMJ1254791",
                serviceId = "VAC_1",
                serviceName = "Vaccination",
                categoryName = "Vaccination",
                price = 350.0,
                date = "28 May 2025",
                timeSlot = "11:00 AM",
                address = "12, Park Street, Bangalore",
                patientName = "Riya Sharma",
                patientAge = 28,
                specialNotes = "Annual flu immunization booster dose.",
                paymentGateway = "PhonePe",
                paymentMethod = "UPI",
                paymentStatus = "PENDING",
                transactionId = "TXN_UPI_661902",
                invoiceNumber = "INV-2026-4403",
                status = AppointmentStatus.PENDING.name,
                assignedNurseId = "NURSE_103",
                assignedNurseName = "Neha Reddy",
                assignedNursePhone = "+91 98765 43212",
                assignedNurseQualification = "Senior Staff Nurse (Vaccines)",
                assignedNurseRating = 4.9
            )

            val appt4 = AppointmentEntity(
                id = "BMJ1254792",
                serviceId = "LAB_1",
                serviceName = "Lab Test",
                categoryName = "Lab Test",
                price = 399.0,
                date = "30 May 2025",
                timeSlot = "09:00 AM",
                address = "12, Park Street, Bangalore",
                patientName = "Riya Sharma",
                patientAge = 28,
                specialNotes = "Sample collection (Complete blood count & fasting sugar).",
                paymentGateway = "Razorpay",
                paymentMethod = "Net Banking",
                paymentStatus = "SUCCESS",
                transactionId = "TXN_NB_339182",
                invoiceNumber = "INV-2026-4404",
                status = AppointmentStatus.CONFIRMED.name,
                assignedNurseId = "NURSE_104",
                assignedNurseName = "Kavita Nair",
                assignedNursePhone = "+91 98765 43213",
                assignedNurseQualification = "Certified Phlebotomist & Nurse",
                assignedNurseRating = 4.8
            )

            appointmentDao.insertAppointment(appt1)
            appointmentDao.insertAppointment(appt2)
            appointmentDao.insertAppointment(appt3)
            appointmentDao.insertAppointment(appt4)
        }
    }

    // --- User Management ---
    suspend fun loginOrRegisterUser(
        phone: String,
        name: String,
        email: String,
        address: String,
        pincode: String,
        emergencyContact: String,
        bloodGroup: String,
        medicalNotes: String
    ) {
        userDao.logoutAll()
        val user = UserEntity(
            phone = phone,
            name = name,
            email = email,
            address = address,
            pincode = pincode,
            emergencyContact = emergencyContact,
            bloodGroup = bloodGroup,
            medicalNotes = medicalNotes,
            isLoggedIn = true
        )
        userDao.insertOrUpdateUser(user)
    }

    suspend fun logout() {
        userDao.logoutAll()
    }

    // --- Appointment Management ---
    suspend fun createAppointment(appointment: AppointmentEntity) {
        appointmentDao.insertAppointment(appointment)
    }

    suspend fun getAppointmentByIdSync(id: String): AppointmentEntity? {
        return appointmentDao.getAppointmentByIdSync(id)
    }

    fun getAppointmentById(id: String): Flow<AppointmentEntity?> {
        return appointmentDao.getAppointmentById(id)
    }

    suspend fun cancelAppointment(id: String, reason: String) {
        appointmentDao.cancelAppointment(id, reason)
    }

    suspend fun rescheduleAppointment(id: String, newDate: String, newTimeSlot: String) {
        appointmentDao.rescheduleAppointment(id, newDate, newTimeSlot)
    }

    suspend fun submitRating(id: String, rating: Float, review: String) {
        appointmentDao.submitRating(id, rating, review)
    }

    suspend fun assignNurseToAppointment(id: String, nurse: Nurse) {
        appointmentDao.assignNurse(
            id = id,
            nurseId = nurse.id,
            nurseName = nurse.name,
            nursePhone = nurse.phone,
            qualification = nurse.qualification,
            rating = nurse.rating
        )
    }

    // --- Support Tickets ---
    suspend fun submitSupportTicket(phone: String, subject: String, message: String) {
        val ticket = SupportTicketEntity(
            userPhone = phone,
            subject = subject,
            message = message
        )
        supportTicketDao.insertTicket(ticket)
    }
}
