package com.example.sharedlogic

data class SharedCareService(
    val id: String,
    val name: String,
    val subtitle: String,
    val price: Double,
    val duration: String,
    val icon: String,
    val description: String,
    val category: String,
    val isPopular: Boolean
)

data class SharedCareNurse(
    val id: String,
    val name: String,
    val qualification: String,
    val experience: String,
    val rating: String,
    val phone: String,
    val specialization: String,
    val bio: String
)

object CareCatalog {
    val services: List<SharedCareService> = listOf(
        SharedCareService("NURSING_1", "Home Nursing Visit", "Vitals, injections & wound care", 699.0, "45–60 mins", "cross.case.fill", "A certified nurse visits your home for professional nursing support, vital checks, injections, and dressing changes.", "HOME_NURSING", true),
        SharedCareService("NURSING_2", "24/7 ICU Home Nurse", "Critical care & continuous monitoring", 2499.0, "24 hour shift", "heart.text.square.fill", "Dedicated ICU-trained nursing support for ventilator, tracheostomy, feeding tube, and post-ICU recovery care.", "HOME_NURSING", true),
        SharedCareService("ELDER_1", "Compassionate Elder Care", "Daily routine & companionship", 1199.0, "12 hour shift", "figure.2.and.child.holdinghands", "Respectful assistance with mobility, hygiene, medication reminders, meals, and meaningful companionship.", "ELDER_CARE", true),
        SharedCareService("PHYSIO_1", "Home Physiotherapy", "Rehabilitation at your doorstep", 799.0, "45–60 mins", "figure.walk.motion", "Personalized mobility assessment, therapeutic exercises, posture work, and pain-relief therapy at home.", "PHYSIOTHERAPY", false),
        SharedCareService("LAB_1", "Home Lab Test", "Safe sample collection", 399.0, "20–30 mins", "testtube.2", "Certified phlebotomists collect samples at home with digital reports delivered quickly.", "LAB_TEST", false),
        SharedCareService("VACCINE_1", "Home Vaccination", "Safe, sterile & convenient", 399.0, "30–45 mins", "syringe.fill", "Cold-chain vaccine delivery, professional administration, and post-vaccine observation in the comfort of home.", "VACCINATION", false)
    )

    val nurses: List<SharedCareNurse> = listOf(
        SharedCareNurse("NURSE_101", "Priya Sharma", "B.Sc Nursing (ICU & Home Care)", "5 years experience", "4.8", "+91 98765 43210", "Home Nursing, IV Drips & Elder Care", "Compassionate and experienced nurse specialized in elderly and post-surgical care."),
        SharedCareNurse("NURSE_102", "Ankit Verma", "MPT (Orthopedics Specialist)", "6 years experience", "4.8", "+91 98765 43211", "Joint Mobility & Posture Correction", "Specialized in musculoskeletal rehabilitation, joint mobility restoration, and post-operative orthopedic recovery."),
        SharedCareNurse("NURSE_103", "Neha Reddy", "Senior Staff Nurse (Vaccines & Pediatrics)", "7 years experience", "4.9", "+91 98765 43212", "Vaccinations & Gentle Sample Collection", "Expert in gentle pediatric vaccinations, elder wellness checks, and painless diagnostic sample collection.")
    )
}

fun sharedServices(): List<SharedCareService> = CareCatalog.services

fun sharedNurses(): List<SharedCareNurse> = CareCatalog.nurses

fun sharedAdvisorResponse(query: String): String {
    val prompt = query.lowercase()
    return when {
        "elder" in prompt || "senior" in prompt ->
            "Compassionate Elder Care may be a good fit. It includes mobility assistance, medication reminders, daily routine support, and companionship."
        "surgery" in prompt || "operation" in prompt ->
            "Home Nursing Visit or Home Physiotherapy can support recovery. A nurse can monitor your wound and vitals; a physiotherapist can help restore safe movement."
        "critical" in prompt || "icu" in prompt ->
            "Please speak with your doctor first. For medically approved home recovery, our 24/7 ICU Home Nurse service offers continuous monitoring and critical-care support."
        else ->
            "A Home Nursing Visit is a helpful starting point for vitals, injections, wound care, and a professional assessment. Our care team can guide you to the right package."
    }
}