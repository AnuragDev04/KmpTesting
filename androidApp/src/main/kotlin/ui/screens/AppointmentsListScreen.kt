package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppointmentEntity
import com.example.data.models.AppointmentStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsListScreen(
    viewModel: MainViewModel,
    onAppointmentClick: (String) -> Unit,
    onTrackNurseClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val appointments by viewModel.allAppointments.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Upcoming, 1: Completed, 2: Cancelled

    val tabTitles = listOf("Upcoming", "Completed", "Cancelled")

    val filteredAppointments = remember(appointments, selectedTab) {
        when (selectedTab) {
            0 -> appointments.filter {
                it.status != AppointmentStatus.COMPLETED.name && it.status != AppointmentStatus.CANCELLED.name
            }
            1 -> appointments.filter { it.status == AppointmentStatus.COMPLETED.name }
            2 -> appointments.filter { it.status == AppointmentStatus.CANCELLED.name }
            else -> appointments
        }
    }

    Scaffold(
        containerColor = Color(0xFFFCF9FE),
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1EBF7))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "My Appointments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E153A),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // SEGMENTED TABS (Upcoming | Completed | Cancelled)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Surface(
                    color = Color(0xFFF3EAF8),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color.White else Color.Transparent,
                                shadowElevation = if (isSelected) 2.dp else 0.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedTab = index }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 13.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF9A3412) else Color(0xFF4B5563)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // APPOINTMENTS LIST CONTENT
            // ==========================================
            AnimatedContent(
                targetState = filteredAppointments,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "appointments_list_anim"
            ) { targetList ->
                if (targetList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF3EAF8),
                                modifier = Modifier.size(72.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.EventBusy,
                                        contentDescription = null,
                                        tint = PurplePrimary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                            Text(
                                text = "No ${tabTitles[selectedTab].lowercase()} appointments",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E153A)
                            )
                            Text(
                                text = "Book healthcare services at home with verified nurses.",
                                fontSize = 13.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(targetList, key = { it.id }) { appt ->
                            AppointmentReferenceCard(
                                appointment = appt,
                                onClick = { onAppointmentClick(appt.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 1:1 REFERENCE APPOINTMENT CARD
// ==========================================
@Composable
fun AppointmentReferenceCard(
    appointment: AppointmentEntity,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    // Determine avatar based on nurse or service
    val nurseName = appointment.assignedNurseName?.lowercase() ?: ""
    val serviceName = appointment.serviceName.lowercase()

    val avatarDrawableResName = when {
        nurseName.contains("priya") || serviceName.contains("nursing") -> "nurse_priya_pic_1787034588602"
        nurseName.contains("ankit") || serviceName.contains("physio") -> "nurse_ankit_pic_1787046592042"
        nurseName.contains("neha") || serviceName.contains("vaccin") -> "nurse_neha_pic_1787046614205"
        else -> null
    }

    val isLabTest = serviceName.contains("lab") || serviceName.contains("test")

    val avatarResId = remember(avatarDrawableResName) {
        avatarDrawableResName?.let { res ->
            context.resources.getIdentifier(res, "drawable", context.packageName)
        } ?: 0
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1E8F7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // TOP ROW: Avatar + Details + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Left Circular Avatar / Icon
                    if (isLabTest) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF3E8FF),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Science,
                                    contentDescription = "Lab Test",
                                    tint = Color(0xFFC2410C),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    } else if (avatarResId != 0) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFFEDE9FE), CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = avatarResId),
                                contentDescription = appointment.assignedNurseName ?: "Nurse",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEDE9FE),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PurplePrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    // Middle: Service Name + Date & Time + Description
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = appointment.serviceName,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E153A)
                        )

                        Text(
                            text = "${appointment.date}, ${appointment.timeSlot}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )

                        if (isLabTest) {
                            Text(
                                text = "Sample collection",
                                fontSize = 11.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                // Right Status Badge
                AppointmentStatusPill(status = appointment.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subtle Divider
            HorizontalDivider(
                color = Color(0xFFF3E8FF),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // BOTTOM ROW: Nurse Name + Right Chevron
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val nurseDisplayName = appointment.assignedNurseName ?: if (isLabTest) "Phlebotomist Assigned" else "Qualified Healthcare Provider"
                Text(
                    text = "Nurse: $nurseDisplayName",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E153A)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View Details",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==========================================
// STATUS PILL COMPONENT (Confirmed / Pending / etc.)
// ==========================================
@Composable
private fun AppointmentStatusPill(status: String) {
    val isConfirmed = status.equals("CONFIRMED", ignoreCase = true) ||
            status.equals("Booking Confirmed", ignoreCase = true)
    val isPending = status.equals("PENDING", ignoreCase = true) ||
            status.equals("Pending", ignoreCase = true)
    val isCompleted = status.equals("COMPLETED", ignoreCase = true) ||
            status.equals("Service Completed", ignoreCase = true)
    val isCancelled = status.equals("CANCELLED", ignoreCase = true)

    val (bgColor, textColor, label) = when {
        isConfirmed -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "Confirmed")
        isPending -> Triple(Color(0xFFFEF3C7), Color(0xFFC2410C), "Pending")
        isCompleted -> Triple(Color(0xFFEDE9FE), Color(0xFF6D28D9), "Completed")
        isCancelled -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Cancelled")
        else -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "Confirmed")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(start = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
