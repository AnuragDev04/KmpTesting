package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCheckoutScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val service = viewModel.services.firstOrNull { it.id == serviceId }
        ?: viewModel.services.first()

    val selectedGateway by viewModel.selectedPaymentGateway.collectAsState()
    val selectedMethod by viewModel.selectedPaymentMethod.collectAsState()
    val couponCode by viewModel.couponCode.collectAsState()
    val isCouponApplied by viewModel.isCouponApplied.collectAsState()
    val isProcessing by viewModel.isPaymentProcessing.collectAsState()

    var inputCoupon by remember { mutableStateOf(couponCode) }
    var upiIdInput by remember { mutableStateOf("rahul@okaxis") }
    var cardNumberInput by remember { mutableStateOf("4532 •••• •••• 8812") }

    var couponError by remember { mutableStateOf<String?>(null) }

    val gateways = listOf("Razorpay", "PhonePe", "Stripe", "PayU")
    val methods = listOf("UPI", "Credit/Debit Card", "Net Banking", "Digital Wallets")

    // Calculations
    val basePrice = service.price
    val discount = if (isCouponApplied) basePrice * 0.10 else 0.0
    val priceAfterDiscount = basePrice - discount
    val gstTax = priceAfterDiscount * 0.18
    val grandTotal = priceAfterDiscount + gstTax

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Payment Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Grand Total (incl. GST)", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = "₹${String.format(Locale.US, "%.2f", grandTotal)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealPrimary
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.processBookingAndPayment(service) { newApptId ->
                                onPaymentSuccess(newApptId)
                            }
                        },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Processing...", fontSize = 14.sp)
                        } else {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pay & Confirm", fontSize = 15.sp, fontWeight = FontWeight.Bold)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Appointment Summary Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Booking & Service Summary",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Service:", fontSize = 13.sp, color = TextSecondary)
                        Text(service.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Scheduled Date:", fontSize = 13.sp, color = TextSecondary)
                        Text(viewModel.bookingDate.value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Time Slot:", fontSize = 13.sp, color = TextSecondary)
                        Text(viewModel.bookingTimeSlot.value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Patient Name:", fontSize = 13.sp, color = TextSecondary)
                        Text(viewModel.bookingPatientName.value.ifBlank { "Rahul Malhotra" }, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Payment Gateway Partner Selection
            Column {
                Text(
                    text = "Select Payment Gateway",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    gateways.forEach { gateway ->
                        val isSelected = (selectedGateway == gateway)
                        Surface(
                            color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.selectedPaymentGateway.value = gateway }
                        ) {
                            Text(
                                text = gateway,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Payment Method Selector
            Column {
                Text(
                    text = "Payment Method",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    methods.forEach { method ->
                        val isSelected = (selectedMethod == method)
                        Surface(
                            color = if (isSelected) TealPrimaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TealPrimary) else null,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectedPaymentMethod.value = method }
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = when (method) {
                                                "UPI" -> Icons.Default.QrCodeScanner
                                                "Credit/Debit Card" -> Icons.Default.CreditCard
                                                "Net Banking" -> Icons.Default.AccountBalance
                                                else -> Icons.Default.AccountBalanceWallet
                                            },
                                            contentDescription = null,
                                            tint = if (isSelected) TealPrimary else TextMuted
                                        )
                                        Text(
                                            text = method,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) TealPrimaryDark else MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { viewModel.selectedPaymentMethod.value = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = TealPrimary)
                                    )
                                }

                                if (isSelected && method == "UPI") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = upiIdInput,
                                        onValueChange = { upiIdInput = it },
                                        label = { Text("UPI ID (Google Pay / PhonePe / Paytm)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                if (isSelected && method == "Credit/Debit Card") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = cardNumberInput,
                                        onValueChange = { cardNumberInput = it },
                                        label = { Text("Card Number") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Coupon Code Section
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text("Apply Promo Coupon", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputCoupon,
                            onValueChange = { inputCoupon = it },
                            placeholder = { Text("Enter CARE10 or HEALTH20") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        if (isCouponApplied) {
                            Button(
                                onClick = {
                                    viewModel.removeCoupon()
                                    inputCoupon = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusRed)
                            ) {
                                Text("Remove")
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (viewModel.applyCoupon(inputCoupon)) {
                                        couponError = null
                                    } else {
                                        couponError = "Invalid coupon code. Try CARE10"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                            ) {
                                Text("Apply")
                            }
                        }
                    }

                    if (isCouponApplied) {
                        Text(
                            text = "🎉 10% Healthcare discount applied!",
                            color = StatusGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    if (couponError != null) {
                        Text(
                            text = couponError!!,
                            color = StatusRed,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Cost Calculation Breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Payment Breakdown", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Base Service Rate:", fontSize = 13.sp, color = TextSecondary)
                        Text("₹${String.format(Locale.US, "%.2f", basePrice)}", fontSize = 13.sp)
                    }

                    if (isCouponApplied) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Coupon Discount (10%):", fontSize = 13.sp, color = StatusGreen)
                            Text("- ₹${String.format(Locale.US, "%.2f", discount)}", fontSize = 13.sp, color = StatusGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Healthcare GST (18%):", fontSize = 13.sp, color = TextSecondary)
                        Text("+ ₹${String.format(Locale.US, "%.2f", gstTax)}", fontSize = 13.sp)
                    }

                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Grand Total Amount:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "₹${String.format(Locale.US, "%.2f", grandTotal)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
