package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel

// Brand Color Palette matching Reference
private val ScreenBackground = Color(0xFFFCF8FD)
private val BrandPurple = Color(0xFF6E4D90)          // Primary purple for completed steps and icons
private val BrandPurpleDark = Color(0xFF1E153A)      // Dark navy/purple text
private val BrandPurpleLight = Color(0xFFF3E8FF)     // Soft lavender tint
private val BrandPurpleRing = Color(0xFF6E4D90)      // Step 4 outlined ring
private val StepOrange = Color(0xFFF26822)           // Orange Confirm Booking CTA
private val DiscountGreen = Color(0xFF10B981)        // Positive green discount
private val TextMutedGray = Color(0xFF64748B)        // Subtitle text
private val TextPlaceholder = Color(0xFF94A3B8)      // Placeholder text
private val BorderSubtle = Color(0xFFF1EEF6)         // Card border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBookingScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onEditBooking: () -> Unit,
    onProceedToPayment: (String) -> Unit
) {
    val context = LocalContext.current

    val service = remember(serviceId) {
        viewModel.services.firstOrNull { it.id.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.firstOrNull { it.category.name.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.first()
    }

    val bookingDate by viewModel.bookingDate.collectAsState()
    val bookingTimeSlot by viewModel.bookingTimeSlot.collectAsState()
    val bookingAddress by viewModel.bookingAddress.collectAsState()
    var specialInstructions by remember { mutableStateOf(viewModel.bookingSpecialNotes.value) }

    val isProcessing by viewModel.isPaymentProcessing.collectAsState()

    // Formatted display values matching reference
    val displayServiceName = remember(service.name) {
        if (service.name.isNotBlank()) service.name else "Home Nursing"
    }

    val displayDateTime = remember(bookingDate, bookingTimeSlot) {
        val dateText = if (bookingDate.isNotBlank()) {
            bookingDate.replace("May 2025", "May 2025,").ifEmpty { "24 May 2025," }
        } else {
            "24 May 2025,"
        }
        val timeText = if (bookingTimeSlot.isNotBlank()) bookingTimeSlot else "10:00 AM"
        if (dateText.contains(",")) "$dateText $timeText" else "$dateText, $timeText"
    }

    val displayAddress = remember(bookingAddress) {
        if (bookingAddress.isNotBlank()) {
            val firstLine = bookingAddress.split(",").take(3).joinToString(",")
            if (firstLine.isNotBlank()) firstLine else "12, Park Street, Bangalore"
        } else {
            "12, Park Street, Bangalore"
        }
    }

    // Dynamic Pricing Calculation matching reference
    val serviceFee = remember(service.price) {
        if (service.price > 0) service.price.toInt() else 899
    }
    val platformFee = 20
    val discount = 180 // CARE20 coupon
    val totalAmount = remember(serviceFee, platformFee, discount) {
        (serviceFee + platformFee - discount).coerceAtLeast(0)
    }

    // Resolve Nurse Avatar Image
    val nurseAvatarResId = remember {
        val customAvatar = context.resources.getIdentifier("user_avatar_riya_1787034560071", "drawable", context.packageName)
        if (customAvatar != 0) customAvatar else {
            context.resources.getIdentifier("nurse_elder_care_illus_1787041281294", "drawable", context.packageName)
        }
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            // ==========================================
            // HEADER (Back Arrow + Confirm Booking Title)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("confirm_booking_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPurpleDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Confirm Booking",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM CTA (Orange Confirm Booking Button)
            // ==========================================
            Surface(
                color = ScreenBackground,
                shadowElevation = 0.dp,
                border = BorderStroke(0.5.dp, Color(0xFFF1EEF6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.bookingSpecialNotes.value = specialInstructions
                            onProceedToPayment(service.id)
                        },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StepOrange,
                            disabledContainerColor = StepOrange.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = StepOrange)
                            .testTag("confirm_booking_button")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Confirm Booking",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
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
            Spacer(modifier = Modifier.height(6.dp))

            // ==========================================
            // 4-STEP PROGRESS INDICATOR (Step 4 Confirm Outlined)
            // ==========================================
            ConfirmStepProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // APPOINTMENT SUMMARY CARD (Nurse + Info + Chevron)
            // ==========================================
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditBooking() }
                    .testTag("appointment_summary_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Nurse Image
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF3E8FF),
                        border = BorderStroke(1.5.dp, Color(0xFFEDE9FE)),
                        shadowElevation = 1.dp,
                        modifier = Modifier.size(60.dp)
                    ) {
                        if (nurseAvatarResId != 0) {
                            Image(
                                painter = painterResource(id = nurseAvatarResId),
                                contentDescription = "Nurse Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "👩‍⚕️",
                                    fontSize = 28.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Appointment Details
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = displayServiceName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )

                        // Date & Time Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = "Date",
                                tint = TextMutedGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = displayDateTime,
                                fontSize = 12.5.sp,
                                color = TextMutedGray,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        // Address Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Location",
                                tint = TextMutedGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = displayAddress,
                                fontSize = 12.5.sp,
                                color = TextMutedGray,
                                fontWeight = FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    }

                    // Right-facing Chevron
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Edit details",
                        tint = TextPlaceholder,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // PRICE DETAILS SECTION
            // ==========================================
            Text(
                text = "Price Details",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("price_details_card")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Service Fee Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Service Fee",
                            fontSize = 14.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹$serviceFee",
                            fontSize = 14.5.sp,
                            color = BrandPurpleDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Platform Fee Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Platform Fee",
                            fontSize = 14.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹$platformFee",
                            fontSize = 14.5.sp,
                            color = BrandPurpleDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Discount (CARE20) Row (Positive Green)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discount (CARE20)",
                            fontSize = 14.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "-₹$discount",
                            fontSize = 14.5.sp,
                            color = DiscountGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Horizontal Divider
                    HorizontalDivider(
                        color = Color(0xFFF1F5F9),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Total Amount Row (Bold Extra Large)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount",
                            fontSize = 16.sp,
                            color = BrandPurpleDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹$totalAmount",
                            fontSize = 20.sp,
                            color = BrandPurpleDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SPECIAL INSTRUCTIONS (OPTIONAL) SECTION
            // ==========================================
            Text(
                text = "Special Instructions (Optional)",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = specialInstructions,
                onValueChange = { specialInstructions = it },
                placeholder = {
                    Text(
                        text = "Type your instructions...",
                        fontSize = 14.sp,
                        color = TextPlaceholder
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BrandPurple,
                    unfocusedBorderColor = BorderSubtle
                ),
                minLines = 4,
                maxLines = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("special_instructions_field")
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// ==========================================
// 4-STEP CONFIRM PROGRESS INDICATOR (Step 4 Outlined)
// ==========================================
@Composable
fun ConfirmStepProgressIndicator(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Step 1: Service (Completed Purple Check)
        ConfirmConnectedStepItem(
            stepNumber = 1,
            label = "Service",
            isCompleted = true,
            isOutlined = false,
            modifier = Modifier.weight(1f)
        )

        // Connecting Line 1-2 (Purple)
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(BrandPurple)
        )

        // Step 2: Date & Time (Completed Purple Check)
        ConfirmConnectedStepItem(
            stepNumber = 2,
            label = "Date & Time",
            isCompleted = true,
            isOutlined = false,
            modifier = Modifier.weight(1.2f)
        )

        // Connecting Line 2-3 (Purple)
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(BrandPurple)
        )

        // Step 3: Address (Completed Purple Check)
        ConfirmConnectedStepItem(
            stepNumber = 3,
            label = "Address",
            isCompleted = true,
            isOutlined = false,
            modifier = Modifier.weight(1f)
        )

        // Connecting Line 3-4 (Purple)
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(2.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(BrandPurple)
        )

        // Step 4: Confirm (Outlined Circle with Purple 4)
        ConfirmConnectedStepItem(
            stepNumber = 4,
            label = "Confirm",
            isCompleted = false,
            isOutlined = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ConfirmConnectedStepItem(
    stepNumber: Int,
    label: String,
    isCompleted: Boolean,
    isOutlined: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCompleted) {
            // Completed: Purple filled circle with white checkmark
            Surface(
                shape = CircleShape,
                color = BrandPurple,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else if (isOutlined) {
            // Active Step 4: Purple outlined circle with purple numeral 4
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(2.dp, BrandPurpleRing),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$stepNumber",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPurpleDark
                    )
                }
            }
        } else {
            // Upcoming
            Surface(
                shape = CircleShape,
                color = BrandPurpleLight,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$stepNumber",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPurpleDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isOutlined) FontWeight.Bold else FontWeight.Medium,
            color = if (isOutlined) BrandPurpleDark else TextMutedGray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
