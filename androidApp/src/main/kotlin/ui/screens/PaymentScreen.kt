package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Language
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel

// Color Palette matching Reference Image (Payment.png)
private val ScreenBackground = Color(0xFFFCF8FD)
private val BrandPurpleDark = Color(0xFF1E153A)      // Deep dark navy/purple heading and title
private val BrandPurple = Color(0xFF674486)          // Primary purple for Apply button & accents
private val BrandPurpleLight = Color(0xFFF3E8FF)     // Soft lavender icon container
private val PayNowButtonColor = Color(0xFFA04400)    // Warm brown/orange Pay Now CTA button
private val BorderPeach = Color(0xFFF3E7DC)          // Subtle peach/beige card outline
private val TextMutedGray = Color(0xFF64748B)        // Subtitle text
private val TextPlaceholder = Color(0xFF94A3B8)      // Input placeholder text
private val SuccessGreen = Color(0xFF10B981)         // Coupon success badge

enum class PaymentOption(val title: String, val icon: ImageVector) {
    UPI("UPI", Icons.Outlined.AccountBalance),
    CARD("Credit / Debit Card", Icons.Outlined.CreditCard),
    NET_BANKING("Net Banking", Icons.Outlined.Language),
    WALLETS("Wallets", Icons.Outlined.AccountBalanceWallet)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val context = LocalContext.current

    val service = remember(serviceId) {
        viewModel.services.firstOrNull { it.id.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.firstOrNull { it.category.name.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.first()
    }

    val bookingDate by viewModel.bookingDate.collectAsState()
    val bookingTimeSlot by viewModel.bookingTimeSlot.collectAsState()
    val isProcessing by viewModel.isPaymentProcessing.collectAsState()

    // Selected Payment Method (Default to UPI matching reference)
    var selectedPaymentMethod by remember { mutableStateOf(PaymentOption.UPI) }

    // Coupon Code State (Supports CARE20 from previous flow)
    var couponInput by remember { mutableStateOf("CARE20") }
    var isCouponApplied by remember { mutableStateOf(true) }
    var couponMessage by remember { mutableStateOf<String?>("CARE20 applied (₹180 OFF)") }

    // Dynamic Price Calculation
    val baseServiceFee = remember(service.price) {
        if (service.price > 0) service.price.toInt() else 899
    }
    val platformFee = 20
    val discountAmount = if (isCouponApplied) 180 else 0
    val totalPayable = remember(baseServiceFee, platformFee, discountAmount) {
        (baseServiceFee + platformFee - discountAmount).coerceAtLeast(0)
    }

    // Formatted Date & Time Display
    val displayDateTime = remember(bookingDate, bookingTimeSlot) {
        val dateText = if (bookingDate.isNotBlank()) {
            bookingDate.replace("May 2025", "May 2025,").ifEmpty { "24 May 2025," }
        } else {
            "24 May 2025,"
        }
        val timeText = if (bookingTimeSlot.isNotBlank()) bookingTimeSlot else "10:00 AM"
        if (dateText.contains(",")) "$dateText $timeText" else "$dateText, $timeText"
    }

    // Resolve Healthcare/Home Nursing Service Illustration Asset
    val serviceIllustrationResId = remember {
        val customNurse = context.resources.getIdentifier("user_avatar_riya_1787034560071", "drawable", context.packageName)
        if (customNurse != 0) customNurse else {
            context.resources.getIdentifier("nurse_elder_care_illus_1787041281294", "drawable", context.packageName)
        }
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            // ==========================================
            // HEADER (Back Arrow + Payment Title)
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
                        .testTag("payment_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPurpleDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Payment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM PAYMENT SECTION (Total Payable + Pay Now)
            // ==========================================
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, Color(0xFFF1EEF6)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Total Payable Amount
                    Column {
                        Text(
                            text = "Total Payable",
                            fontSize = 12.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "₹$totalPayable",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BrandPurpleDark
                        )
                    }

                    // Right: Pay Now Button
                    Button(
                        onClick = {
                            viewModel.processBookingAndPayment(service) { newApptId ->
                                onPaymentSuccess(newApptId)
                            }
                        },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PayNowButtonColor,
                            disabledContainerColor = PayNowButtonColor.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp)
                            .shadow(3.dp, RoundedCornerShape(14.dp), spotColor = PayNowButtonColor)
                            .testTag("pay_now_button")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Pay Now",
                                fontSize = 15.5.sp,
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
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // APPOINTMENT SUMMARY SECTION
            // ==========================================
            Text(
                text = "Appointment Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderPeach),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_appointment_summary_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Service Image Container
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrandPurpleLight,
                        modifier = Modifier.size(52.dp)
                    ) {
                        if (serviceIllustrationResId != 0) {
                            Image(
                                painter = painterResource(id = serviceIllustrationResId),
                                contentDescription = "Service Illustration",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🩺", fontSize = 24.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Appointment Details Info
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (service.name.isNotBlank()) service.name else "Home Nursing",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = displayDateTime,
                            fontSize = 12.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "1 Hour",
                            fontSize = 12.sp,
                            color = TextMutedGray,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    // Price Amount (₹739)
                    Text(
                        text = "₹$totalPayable",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandPurpleDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // COUPON CODE SECTION
            // ==========================================
            Text(
                text = "Coupon Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Coupon Input Field
                OutlinedTextField(
                    value = couponInput,
                    onValueChange = {
                        couponInput = it
                        if (it.isBlank()) {
                            isCouponApplied = false
                            couponMessage = null
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Enter coupon code",
                            fontSize = 13.5.sp,
                            color = TextPlaceholder
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = BrandPurple,
                        unfocusedBorderColor = BorderPeach
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("coupon_input_field")
                )

                // Purple Apply Button
                Button(
                    onClick = {
                        val cleanedCode = couponInput.trim().uppercase()
                        if (cleanedCode == "CARE20" || cleanedCode == "HEALTH50" || cleanedCode == "WELCOME") {
                            isCouponApplied = true
                            couponMessage = "$cleanedCode applied successfully! (₹180 OFF)"
                        } else if (cleanedCode.isEmpty()) {
                            isCouponApplied = false
                            couponMessage = null
                        } else {
                            isCouponApplied = false
                            couponMessage = "Invalid coupon code"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 22.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("apply_coupon_button")
                ) {
                    Text(
                        text = "Apply",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Coupon feedback badge
            couponMessage?.let { msg ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = msg,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isCouponApplied) SuccessGreen else Color(0xFFEF4444),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // PAYMENT METHODS SECTION (One large white card with 4 rows)
            // ==========================================
            Text(
                text = "Payment Methods",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderPeach),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_methods_card")
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaymentOption.values().forEachIndexed { index, option ->
                        val isSelected = (selectedPaymentMethod == option)

                        PaymentMethodRowItem(
                            option = option,
                            isSelected = isSelected,
                            onClick = { selectedPaymentMethod = option }
                        )

                        // Divider between rows (except after last row)
                        if (index < PaymentOption.values().size - 1) {
                            HorizontalDivider(
                                color = Color(0xFFF1F5F9),
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// ==========================================
// PAYMENT METHOD ROW ITEM COMPOSABLE
// ==========================================
@Composable
private fun PaymentMethodRowItem(
    option: PaymentOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) Color(0xFFFBF8FF) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Light-Purple Icon Container
        Surface(
            shape = CircleShape,
            color = BrandPurpleLight,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    tint = BrandPurple,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Payment Method Name
        Text(
            text = option.title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurpleDark,
            modifier = Modifier.weight(1f)
        )

        // Right Chevron
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (isSelected) BrandPurple else Color(0xFF94A3B8),
            modifier = Modifier.size(20.dp)
        )
    }
}
