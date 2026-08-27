package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.AppointmentEntity
import com.example.data.models.AppointmentStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

// Custom Color Palette strictly matching Reference (Screenshot Appointment Details.png)
private val ScreenBackground = Color(0xFFFCF8FD)     // Very light lavender/pink background
private val HeaderBrown = Color(0xFF8C3814)          // Rich warm brown/dark purple header text
private val SummaryCardPurple = Color(0xFF653B98)    // Deep solid purple summary card
private val OrangeStatusBadge = Color(0xFFEA580C)    // Vibrant orange status pill
private val TrackingTitleBrown = Color(0xFF8C3814)   // Brown/orange tracking status text
private val ProgressBarActive = Color(0xFF9A3412)    // Warm brown/orange active track
private val ProgressBarInactive = Color(0xFFF3E8FF)  // Light lavender inactive track
private val CardBorderColor = Color(0xFFF1EBF7)      // Light subtle border
private val DarkText = Color(0xFF1E153A)             // Navy/dark purple text
private val MutedText = Color(0xFF64748B)            // Slate muted text
private val AccentPurple = Color(0xFF7C3AED)         // Purple for icons & buttons
private val LightPurpleButtonBg = Color(0xFFF3E8FF)  // Light purple icon button container
private val CancelRed = Color(0xFFB91C1C)            // Red for cancel action

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(
    appointmentId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onTrackNurseClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val appointments by viewModel.allAppointments.collectAsState()
    val appointment = remember(appointmentId, appointments) {
        appointments.firstOrNull { it.id == appointmentId } ?: appointments.firstOrNull()
    }

    var showCancelConfirmDialog by remember { mutableStateOf(false) }
    var showRescheduleDialog by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }

    var newRescheduleDate by remember { mutableStateOf("25 May 2025") }
    var newRescheduleSlot by remember { mutableStateOf("11:00 AM") }

    if (appointment == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Appointment record not found.", color = DarkText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = SummaryCardPurple)
                ) {
                    Text("Back to Appointments")
                }
            }
        }
        return
    }

    val isConfirmed = appointment.status.equals("CONFIRMED", ignoreCase = true) ||
            appointment.status.equals("Booking Confirmed", ignoreCase = true)
    val isPending = appointment.status.equals("PENDING", ignoreCase = true) ||
            appointment.status.equals("Pending", ignoreCase = true)
    val isCompleted = appointment.status.equals("COMPLETED", ignoreCase = true) ||
            appointment.status.equals("Service Completed", ignoreCase = true)
    val isCancelled = appointment.status.equals("CANCELLED", ignoreCase = true)

    Scaffold(
        containerColor = ScreenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ==========================================
            // HEADER: Back Arrow + Appointment Details
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = HeaderBrown,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Appointment Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeaderBrown
                )
            }

            // ==========================================
            // PURPLE APPOINTMENT SUMMARY CARD
            // ==========================================
            AppointmentSummaryCard(
                appointment = appointment,
                statusLabel = if (isConfirmed) "Confirmed" else if (isPending) "Pending" else if (isCompleted) "Completed" else "Cancelled",
                statusBgColor = if (isConfirmed) OrangeStatusBadge else if (isPending) Color(0xFFF59E0B) else if (isCompleted) Color(0xFF22C55E) else Color(0xFFEF4444)
            )

            // ==========================================
            // LIVE TRACKING SECTION (For Active/Confirmed Bookings)
            // ==========================================
            if (!isCancelled && !isCompleted) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Live Tracking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    LiveTrackingCard(
                        statusTitle = if (isPending) "Appointment Scheduled" else "Nurse is on the way",
                        etaText = if (isPending) "Scheduled for ${appointment.timeSlot}" else "ETA: 15 mins",
                        onOpenMap = {
                            onTrackNurseClick(appointment.id)
                        }
                    )
                }
            }

            // ==========================================
            // NURSE / HEALTHCARE PROVIDER CARD
            // ==========================================
            NurseInformationCard(
                nurseName = appointment.assignedNurseName ?: "Priya Sharma",
                nurseRole = appointment.assignedNurseQualification ?: "Staff Nurse",
                rating = appointment.assignedNurseRating ?: 4.8,
                reviewsCount = 120,
                phone = appointment.assignedNursePhone ?: "+91 98765 43210",
                onCallClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${appointment.assignedNursePhone ?: "+919876543210"}"))
                    context.startActivity(intent)
                },
                onMessageClick = {
                    showChatDialog = true
                }
            )

            // ==========================================
            // ADDRESS SECTION
            // ==========================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Address",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                AddressCard(
                    address = appointment.address.ifBlank { "12, Park Street, Bangalore, Karnataka - 560001" },
                    onViewOnMap = {
                        showMapDialog = true
                    }
                )
            }

            // ==========================================
            // ACTION BUTTONS (Reschedule & Cancel Appointment)
            // ==========================================
            if (!isCancelled && !isCompleted) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Reschedule Button
                    OutlinedButton(
                        onClick = { showRescheduleDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, SummaryCardPurple),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("reschedule_button")
                    ) {
                        Text(
                            text = "Reschedule",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SummaryCardPurple
                        )
                    }

                    // Cancel Appointment Button
                    Button(
                        onClick = { showCancelConfirmDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CancelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("cancel_button")
                    ) {
                        Text(
                            text = "Cancel\nAppointment",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }
                }
            } else if (isCancelled) {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = CancelRed,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "This appointment has been cancelled.",
                            color = CancelRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // ==========================================
    // CANCEL CONFIRMATION DIALOG
    // ==========================================
    if (showCancelConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showCancelConfirmDialog = false },
            title = {
                Text(
                    text = "Cancel Appointment?",
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to cancel your ${appointment.serviceName} appointment? Any advance fees will be refunded to your original payment method.",
                    fontSize = 14.sp,
                    color = MutedText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelAppointment(appointment.id, "User requested cancellation")
                        showCancelConfirmDialog = false
                        Toast.makeText(context, "Appointment cancelled successfully.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CancelRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancel Appointment", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelConfirmDialog = false }
                ) {
                    Text("Keep Appointment", color = DarkText, fontWeight = FontWeight.Medium)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // ==========================================
    // RESCHEDULE DIALOG
    // ==========================================
    if (showRescheduleDialog) {
        val dateOptions = listOf("Tomorrow, 25 May", "26 May 2025", "27 May 2025", "28 May 2025")
        val slotOptions = listOf("09:00 AM", "11:00 AM", "02:00 PM", "04:30 PM", "06:00 PM")
        var selectedDateIndex by remember { mutableIntStateOf(0) }
        var selectedSlotIndex by remember { mutableIntStateOf(1) }

        Dialog(onDismissRequest = { showRescheduleDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reschedule Appointment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        IconButton(
                            onClick = { showRescheduleDialog = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MutedText)
                        }
                    }

                    Text("Select New Date", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dateOptions.take(2).forEachIndexed { index, dateStr ->
                            val isSel = selectedDateIndex == index
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) SummaryCardPurple else Color(0xFFF3E8FF),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDateIndex = index }
                            ) {
                                Text(
                                    text = dateStr,
                                    color = if (isSel) Color.White else DarkText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
                                )
                            }
                        }
                    }

                    Text("Select Time Slot", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        slotOptions.take(3).forEachIndexed { index, slotStr ->
                            val isSel = selectedSlotIndex == index
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) SummaryCardPurple else Color(0xFFF3E8FF),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedSlotIndex = index }
                            ) {
                                Text(
                                    text = slotStr,
                                    color = if (isSel) Color.White else DarkText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            val newDate = dateOptions[selectedDateIndex]
                            val newSlot = slotOptions[selectedSlotIndex]
                            viewModel.rescheduleAppointment(appointment.id, newDate, newSlot)
                            showRescheduleDialog = false
                            Toast.makeText(context, "Appointment rescheduled to $newDate at $newSlot", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SummaryCardPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Confirm Reschedule", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // ==========================================
    // IN-APP NURSE CHAT DIALOG
    // ==========================================
    if (showChatDialog) {
        var chatInput by remember { mutableStateOf("") }
        val chatMessages = remember {
            mutableStateListOf(
                "Hello! I am ${appointment.assignedNurseName ?: "Nurse Priya"}. I am en route to your location.",
                "Please ensure the patient has rested and any medical prescriptions are ready."
            )
        }

        Dialog(onDismissRequest = { showChatDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Chat Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(shape = CircleShape, color = Color(0xFFEDE9FE), modifier = Modifier.size(38.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(22.dp))
                                }
                            }
                            Column {
                                Text(appointment.assignedNurseName ?: "Priya Sharma", fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = DarkText)
                                Text("Online • Nurse Assigned", fontSize = 11.sp, color = Color(0xFF16A34A))
                            }
                        }

                        IconButton(onClick = { showChatDialog = false }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MutedText)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF1EBF7))

                    // Messages List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF3E8FF),
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                Text(
                                    text = msg,
                                    fontSize = 12.5.sp,
                                    color = DarkText,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message Input Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Type a message...", fontSize = 12.5.sp) },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )

                        IconButton(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    chatMessages.add(chatInput)
                                    chatInput = ""
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = SummaryCardPurple),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // MAP VIEW DIALOG
    // ==========================================
    if (showMapDialog) {
        Dialog(onDismissRequest = { showMapDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Address Location", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DarkText)
                        IconButton(onClick = { showMapDialog = false }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MutedText)
                        }
                    }

                    // Simulated Map Container
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = SummaryCardPurple, modifier = Modifier.size(36.dp))
                                Text("12, Park Street, Bangalore", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkText)
                                Text("Karnataka - 560001", fontSize = 11.5.sp, color = MutedText)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val gmmIntentUri = Uri.parse("geo:12.9716,77.5946?q=12,+Park+Street,+Bangalore")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            context.startActivity(mapIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SummaryCardPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open in Google Maps", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 1:1 PURPLE APPOINTMENT SUMMARY CARD
// ==========================================
@Composable
fun AppointmentSummaryCard(
    appointment: AppointmentEntity,
    statusLabel: String,
    statusBgColor: Color
) {
    val context = LocalContext.current
    val isLabTest = appointment.serviceName.contains("lab", ignoreCase = true) ||
            appointment.serviceName.contains("test", ignoreCase = true)

    val avatarDrawableResName = when {
        isLabTest -> null
        appointment.assignedNurseName?.contains("priya", ignoreCase = true) == true -> "nurse_avatar_illus_1787046897395"
        appointment.assignedNurseName?.contains("ankit", ignoreCase = true) == true -> "nurse_ankit_pic_1787046592042"
        appointment.assignedNurseName?.contains("neha", ignoreCase = true) == true -> "nurse_neha_pic_1787046614205"
        else -> "nurse_avatar_illus_1787046897395"
    }

    val avatarResId = remember(avatarDrawableResName) {
        avatarDrawableResName?.let { res ->
            context.resources.getIdentifier(res, "drawable", context.packageName)
        } ?: 0
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SummaryCardPurple),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("appointment_summary_card")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            // Top Section: Avatar + Service Info + Chevron
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
                    // Circular Avatar Container (matching orange/amber circle in reference image)
                    if (isLabTest) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.size(58.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Science,
                                    contentDescription = "Lab Test",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    } else if (avatarResId != 0) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
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
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.size(58.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                    // Middle Column: Title + Date/Time + Booking ID
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = appointment.serviceName,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "${appointment.date}, ${appointment.timeSlot}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Text(
                            text = "Booking ID: ${appointment.id.ifBlank { "BMJ1254789" }}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Right-facing Chevron
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card Inner Subtle Divider
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.2f),
                thickness = 0.8.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Section: Status label + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status:",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Surface(
                    color = statusBgColor,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF431407),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 1:1 LIVE TRACKING CARD
// ==========================================
@Composable
fun LiveTrackingCard(
    statusTitle: String,
    etaText: String,
    onOpenMap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMap() }
            .testTag("live_tracking_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status + Vehicle Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = statusTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrackingTitleBrown
                    )
                    Text(
                        text = etaText,
                        fontSize = 13.5.sp,
                        color = MutedText
                    )
                }

                // Vehicle Container Icon
                Surface(
                    shape = CircleShape,
                    color = LightPurpleButtonBg,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Vehicle",
                            tint = AccentPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Tracking Progress Bar (Home -> En Route -> Arrived)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(ProgressBarInactive)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.65f) // En Route progress
                        .clip(RoundedCornerShape(4.dp))
                        .background(ProgressBarActive)
                )
            }

            // Progress Labels (Home, En Route, Arrived)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedText
                )

                Text(
                    text = "En Route",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TrackingTitleBrown
                )

                Text(
                    text = "Arrived",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedText
                )
            }
        }
    }
}

// ==========================================
// 1:1 NURSE INFORMATION CARD
// ==========================================
@Composable
fun NurseInformationCard(
    nurseName: String,
    nurseRole: String,
    rating: Double,
    reviewsCount: Int,
    phone: String,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    val context = LocalContext.current

    val avatarDrawableResName = when {
        nurseName.contains("priya", ignoreCase = true) -> "nurse_priya_pic_1787034588602"
        nurseName.contains("ankit", ignoreCase = true) -> "nurse_ankit_pic_1787046592042"
        nurseName.contains("neha", ignoreCase = true) -> "nurse_neha_pic_1787046614205"
        else -> "nurse_priya_pic_1787034588602"
    }

    val avatarResId = remember(avatarDrawableResName) {
        avatarDrawableResName.let { res ->
            context.resources.getIdentifier(res, "drawable", context.packageName)
        }
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("nurse_info_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Profile Photo with Warm Border
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFF97316), CircleShape)
                ) {
                    if (avatarResId != 0) {
                        Image(
                            painter = painterResource(id = avatarResId),
                            contentDescription = nurseName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEDE9FE),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(30.dp))
                            }
                        }
                    }
                }

                // Name, Role & Rating
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = nurseName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        text = nurseRole,
                        fontSize = 13.sp,
                        color = MutedText
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = String.format("%.1f", rating),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "($reviewsCount Reviews)",
                            fontSize = 12.sp,
                            color = MutedText
                        )
                    }
                }
            }

            // Call & Message Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Call Button
                IconButton(
                    onClick = onCallClick,
                    modifier = Modifier
                        .size(42.dp)
                        .background(LightPurpleButtonBg, CircleShape)
                        .testTag("call_nurse_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Nurse",
                        tint = AccentPurple,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Message Button
                IconButton(
                    onClick = onMessageClick,
                    modifier = Modifier
                        .size(42.dp)
                        .background(LightPurpleButtonBg, CircleShape)
                        .testTag("message_nurse_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Message Nurse",
                        tint = AccentPurple,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 1:1 ADDRESS CARD
// ==========================================
@Composable
fun AddressCard(
    address: String,
    onViewOnMap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("address_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = AccentPurple,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = address,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "View on Map",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = AccentPurple,
                modifier = Modifier
                    .clickable { onViewOnMap() }
                    .padding(4.dp)
                    .testTag("view_on_map_action")
            )
        }
    }
}
