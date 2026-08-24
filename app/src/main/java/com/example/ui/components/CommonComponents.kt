package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppointmentStatus
import com.example.data.models.HealthcareService
import com.example.data.models.Nurse
import com.example.data.models.ServiceCategory
import com.example.ui.theme.*

@Composable
fun StatusBadge(statusName: String, modifier: Modifier = Modifier) {
    val status = try {
        AppointmentStatus.valueOf(statusName)
    } catch (e: Exception) {
        AppointmentStatus.CONFIRMED
    }

    val (bgColor, textColor, icon) = when (status) {
        AppointmentStatus.CONFIRMED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.CheckCircle)
        AppointmentStatus.PENDING -> Triple(Color(0xFFFEF3C7), Color(0xFFC2410C), Icons.Default.HourglassEmpty)
        AppointmentStatus.NURSE_ASSIGNED -> Triple(Color(0xFFE0E7FF), Color(0xFF4338CA), Icons.Default.Person)
        AppointmentStatus.EN_ROUTE -> Triple(Color(0xFFFFEDD5), Color(0xFFC2410C), Icons.Default.Navigation)
        AppointmentStatus.IN_PROGRESS -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), Icons.Default.HourglassTop)
        AppointmentStatus.COMPLETED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.Verified)
        AppointmentStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), Icons.Default.Cancel)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = status.displayName,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Double,
    reviewsCount: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = String.format("%.1f", rating),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (reviewsCount != null) {
            Text(
                text = "($reviewsCount)",
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun ServiceCategoryChip(
    category: ServiceCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) TealPrimary else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            val icon = when (category) {
                ServiceCategory.HOME_NURSING -> Icons.Default.MedicalServices
                ServiceCategory.VACCINATION -> Icons.Default.Vaccines
                ServiceCategory.ELDER_CARE -> Icons.Default.Elderly
                ServiceCategory.PATIENT_CARE -> Icons.Default.PersonalInjury
                ServiceCategory.MEDICAL_ASSISTANCE -> Icons.Default.MonitorHeart
                ServiceCategory.PHYSIOTHERAPY -> Icons.Default.Accessibility
                ServiceCategory.LAB_TEST -> Icons.Default.Science
                ServiceCategory.EMERGENCY_CARE -> Icons.Default.Shield
                ServiceCategory.HOSPITALITY -> Icons.Default.VolunteerActivism
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = category.displayName,
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun ServiceCard(
    service: HealthcareService,
    onClick: () -> Unit,
    onBookNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageResId = service.imageDrawableRes?.let { resName ->
        context.resources.getIdentifier(resName, "drawable", context.packageName)
    } ?: 0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = service.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Category pill
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = service.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (service.isPopular) {
                    Surface(
                        color = CoralHighlight,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "Popular",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    StarRatingBar(rating = service.rating, reviewsCount = service.reviewsCount)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = service.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${service.price.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TealPrimary
                            )
                            Text(
                                text = " / ${service.priceUnit}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Text(
                            text = "⏱ ${service.duration}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = onBookNow,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Book Now", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NearbyNurseCard(
    nurse: Nurse,
    distanceText: String = "1.2 km away",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val nurseAvatarResId = remember {
        context.resources.getIdentifier("nurse_priya_pic_1787034588602", "drawable", context.packageName)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Circular Nurse Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PurpleLightContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (nurseAvatarResId != 0) {
                        Image(
                            painter = painterResource(id = nurseAvatarResId),
                            contentDescription = nurse.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = nurse.name,
                            tint = PurplePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Nurse Info
                Column {
                    Text(
                        text = nurse.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${nurse.rating} (${nurse.reviewsCount})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${nurse.experienceYears} Years Exp.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Distance on the right
            Text(
                text = distanceText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun HealthcareBottomBar(
    currentRoute: String?,
    onNavigateHome: () -> Unit,
    onNavigateAppointments: () -> Unit,
    onBookNowClick: () -> Unit,
    onNavigateServices: () -> Unit,
    onNavigateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shadowElevation = 10.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(68.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = currentRoute == "home",
                    onClick = onNavigateHome,
                    modifier = Modifier.weight(1f)
                )

                // 2. Appointments
                BottomNavItem(
                    icon = Icons.Default.CalendarMonth,
                    label = "Appointments",
                    isSelected = currentRoute == "appointments",
                    onClick = onNavigateAppointments,
                    modifier = Modifier.weight(1f)
                )

                // 3. Middle placeholder spacer for the floating Book Now button
                Spacer(modifier = Modifier.weight(1f))

                // 4. Services
                BottomNavItem(
                    icon = Icons.Default.MedicalServices,
                    label = "Services",
                    isSelected = currentRoute == "all_services" || currentRoute == "services" || currentRoute?.startsWith("service_detail") == true,
                    onClick = onNavigateServices,
                    modifier = Modifier.weight(1f)
                )

                // 5. Profile
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    isSelected = currentRoute == "profile",
                    onClick = onNavigateProfile,
                    modifier = Modifier.weight(1f)
                )
            }

            // Elevated Center "Book Now" Circular Button overlapping the bar
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-14).dp)
                    .clickable { onBookNowClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    shadowElevation = 6.dp,
                    color = PurplePrimary,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Book Now",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Book Now",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) Color(0xFFF3E8FF) else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) PurplePrimary else Color(0xFF94A3B8),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PurplePrimary else Color(0xFF64748B),
                maxLines = 1
            )
        }
    }
}

