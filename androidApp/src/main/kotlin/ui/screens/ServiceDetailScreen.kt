package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.HealthcareService
import com.example.data.models.ServiceCategory
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ServiceDetailScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit,
    onNurseClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    // Resolve service data dynamically or default to Home Nursing
    val service = remember(serviceId) {
        viewModel.services.firstOrNull { it.id.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.firstOrNull { it.category.name.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.first()
    }

    // Dynamic details tailored per service
    val (durationRange, priceRange, customInclusions, displayName, customDescription) = remember(service.id, service.category) {
        when (service.category) {
            ServiceCategory.HOME_NURSING -> Tuple5(
                "60 - 120\nmins",
                "₹499 -\n₹999",
                listOf(
                    "Vital Signs Monitoring",
                    "Medication Assistance",
                    "Wound Care",
                    "Personal Hygiene"
                ),
                "Home Nursing",
                "Professional nursing care at your home by verified and experienced nurses."
            )
            ServiceCategory.VACCINATION -> Tuple5(
                "30 - 45\nmins",
                "₹399 -\n₹799",
                listOf(
                    "Sterile Cold-Chain Vaccine Delivery",
                    "Professional Nurse Administration",
                    "Post-Vaccine Observation (30 mins)",
                    "Digital Vaccination Certificate"
                ),
                "Vaccination",
                "Safe, sterile all-age vaccinations administered at home by certified medical nurses."
            )
            ServiceCategory.PATIENT_CARE -> Tuple5(
                "4 - 8\nHours",
                "₹999 -\n₹1999",
                listOf(
                    "Post Surgery Recovery Assistance",
                    "Jackson-Pratt Drain & Catheter Care",
                    "Mobility & Repositioning Support",
                    "Pain Monitoring & Medication"
                ),
                "Patient Care",
                "Dedicated post-surgery and chronic recovery assistance tailored for your loved one."
            )
            ServiceCategory.ELDER_CARE -> Tuple5(
                "8 - 12\nHours",
                "₹1199 -\n₹2499",
                listOf(
                    "Bathing & Personal Hygiene Assistance",
                    "Timely Medication Reminders",
                    "Safe Walking & Mobility Support",
                    "Friendly Mental & Social Engagement"
                ),
                "Elder Care",
                "Compassionate, respectful geriatric care providing safety, dignity, and warmth."
            )
            ServiceCategory.MEDICAL_ASSISTANCE -> Tuple5(
                "45 - 60\nmins",
                "₹699 -\n₹1299",
                listOf(
                    "Doctor Video Tele-Consultation",
                    "Portable 12-Lead ECG Recording",
                    "Blood Sugar & Vital Signs Check",
                    "Immediate Digital Prescription"
                ),
                "Medical Assistance",
                "Comprehensive home doctor consultation and diagnostic assessment at your doorstep."
            )
            ServiceCategory.PHYSIOTHERAPY -> Tuple5(
                "45 - 60\nmins",
                "₹799 -\n₹1499",
                listOf(
                    "Joint Mobility & Posture Assessment",
                    "Therapeutic Stretching & Exercise",
                    "Pain Relief Electrotherapy (IFT/TENS)",
                    "Customized Home Exercise Plan"
                ),
                "Physiotherapy",
                "Expert physical rehabilitation and pain relief therapy delivered in the comfort of home."
            )
            ServiceCategory.LAB_TEST -> Tuple5(
                "20 - 30\nmins",
                "₹399 -\n₹899",
                listOf(
                    "100% Sterile Vacuum Tube Blood Draw",
                    "Complete Blood Count (CBC) & Lipid Profile",
                    "Certified Phlebotomist Home Visit",
                    "Digital NABL Report within 6 Hours"
                ),
                "Lab Test",
                "Hassle-free, sterile home diagnostic sample collection with rapid verified test results."
            )
            ServiceCategory.EMERGENCY_CARE -> Tuple5(
                "Immediate\n(20-30m)",
                "₹999 -\n₹1999",
                listOf(
                    "Immediate Critical Care Triage",
                    "Oxygen Saturation & Nebulization",
                    "Emergency IV Cannulation & Meds",
                    "Ambulance Coordination Support"
                ),
                "Emergency Care",
                "Rapid-response emergency care nurse arrival equipped for critical home stabilization."
            )
            else -> Tuple5(
                "60 - 120\nmins",
                "₹499 -\n₹999",
                service.inclusions.ifEmpty {
                    listOf(
                        "Vital Signs Monitoring",
                        "Medication Assistance",
                        "Wound Care",
                        "Personal Hygiene"
                    )
                },
                service.name,
                service.description
            )
        }
    }

    // Resolve primary hero illustration
    val illustrationResId = remember {
        val customArt = context.resources.getIdentifier("nurse_elder_care_illus_1787041281294", "drawable", context.packageName)
        if (customArt != 0) customArt else {
            val promoArt = context.resources.getIdentifier("nurse_promo_art_1787034572378", "drawable", context.packageName)
            if (promoArt != 0) promoArt else {
                context.resources.getIdentifier("img_hero_banner_1786014371290", "drawable", context.packageName)
            }
        }
    }

    val nurseAvatar1 = remember {
        context.resources.getIdentifier("user_avatar_riya_1787034560071", "drawable", context.packageName)
    }

    Scaffold(
        containerColor = Color(0xFFFCF8FC), // Soft pastel lavender-pink background matching reference
        topBar = {
            // ==========================================
            // COMPACT TOP HEADER
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Back Button (Light Purple Circular Container)
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF3E8FF),
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() }
                        .testTag("service_detail_back_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color(0xFF1E1B4B),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Favorite Button (Light Purple Circular Container)
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF3E8FF),
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterEnd)
                        .clickable { isFavorite = !isFavorite }
                        .testTag("service_detail_favorite_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF6B21A8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM BOOKING CTA (Orange Button)
            // ==========================================
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = { onBookClick(service.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF26822) // Vibrant Orange CTA matching reference
                        ),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("service_detail_book_button")
                    ) {
                        Text(
                            text = "Book Appointment",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // ==========================================
            // SERVICE IMAGE CARD (White Rounded Card)
            // ==========================================
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("service_detail_hero_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Small "Service Details" header label
                    Text(
                        text = "Service Details",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1B4B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Large Healthcare Illustration
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFFFF9F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (illustrationResId != 0) {
                            Image(
                                painter = painterResource(id = illustrationResId),
                                contentDescription = "Healthcare illustration",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // SERVICE TITLE & RATING
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B4B),
                    modifier = Modifier.weight(1f)
                )

                // Rating Pill (e.g. ⭐ 4.8 (1300+))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF3E8FF),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "⭐", fontSize = 11.sp)
                        Text(
                            text = "4.8 (1300+)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF581C87)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Service Description
            Text(
                text = customDescription,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF64748B),
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // DURATION & PRICE CARDS (2-Column Grid)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Duration Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF4EEFD), // Very light lavender
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEDE9FE),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = "Duration",
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Duration",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = durationRange,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Price Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF4EEFD), // Very light lavender
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEDE9FE),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Payments,
                                    contentDescription = "Price",
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Price",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = priceRange,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // INCLUDES SECTION (Vertical Checklist)
            // ==========================================
            Text(
                text = "Includes",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1B4B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                customInclusions.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = item,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // AVAILABLE NURSES SECTION (Avatar Row)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNurseClick("NURSE_101") },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Nurses",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B4B)
                )

                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7C3AED)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Nurse Avatars
                listOf(
                    Triple("NURSE_101", "Priya Sharma", Color(0xFFFED7AA)),
                    Triple("NURSE_102", "Ankit Verma", Color(0xFFBFDBFE)),
                    Triple("NURSE_103", "Neha Reddy", Color(0xFFA7F3D0))
                ).forEachIndexed { index, (nId, name, fallbackColor) ->
                    Surface(
                        shape = CircleShape,
                        color = fallbackColor,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { onNurseClick(nId) }
                    ) {
                        if (index == 0) {
                            val priyaHeroRes = context.resources.getIdentifier("nurse_priya_hero_1787048302741", "drawable", context.packageName)
                            val finalPriyaRes = if (priyaHeroRes != 0) priyaHeroRes else nurseAvatar1
                            if (finalPriyaRes != 0) {
                                Image(
                                    painter = painterResource(id = finalPriyaRes),
                                    contentDescription = name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.take(1),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                // Final "+12" circular badge
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF3E8FF),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onNurseClick("NURSE_101") }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+12",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF581C87)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Helper container class
data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
