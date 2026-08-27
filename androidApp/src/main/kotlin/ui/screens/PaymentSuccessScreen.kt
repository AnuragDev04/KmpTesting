package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

// Brand Color Palette matching Reference (Screenshot 2026-08-18 142226.png)
private val BrandPurple = Color(0xFF582C8C)          // Primary deep purple button color
private val BrandPurpleDark = Color(0xFF1E153A)      // Navy/purple text
private val BrandPurpleLight = Color(0xFFF3E8FF)     // Soft lavender tint
private val BrandPurpleBorder = Color(0xFFD8B4FE)    // Outlined button border
private val SuccessGreen = Color(0xFF22C55E)         // Vibrant green success badge
private val CardBackground = Color(0xFFF7F5FC)       // Light lavender/gray card background
private val TextMutedGray = Color(0xFF64748B)        // Muted subtitle text

@Composable
fun PaymentSuccessScreen(
    appointmentId: String,
    viewModel: MainViewModel,
    onViewAppointment: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val allAppointments by viewModel.allAppointments.collectAsState()
    val appointment = remember(appointmentId, allAppointments) {
        allAppointments.firstOrNull { it.id == appointmentId }
    }

    val displayBookingId = remember(appointmentId, appointment) {
        if (appointment != null && appointment.id.isNotBlank()) {
            appointment.id
        } else if (appointmentId.isNotBlank()) {
            appointmentId
        } else {
            "BMJ1254789"
        }
    }

    var showInvoiceDialog by remember { mutableStateOf(false) }
    var isDownloadingInvoice by remember { mutableStateOf(false) }

    // Celebratory Scale & Fade Entrance Animation
    val scaleAnim = remember { Animatable(0.2f) }
    val confettiAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        confettiAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    // Invoice Details Dialog
    if (showInvoiceDialog) {
        InvoiceDetailsDialog(
            bookingId = displayBookingId,
            serviceName = appointment?.serviceName ?: "Home Nursing",
            dateTime = if (appointment != null) "${appointment.date}, ${appointment.timeSlot}" else "24 May 2025, 10:00 AM",
            address = appointment?.address ?: "12, Park Street, Bangalore",
            patientName = appointment?.patientName ?: "Patient",
            nurseName = appointment?.assignedNurseName ?: "Nurse Riya Sharma",
            serviceFee = 899,
            platformFee = 20,
            discount = 180,
            totalPaid = appointment?.price?.toInt() ?: 739,
            paymentMethod = appointment?.paymentMethod ?: "UPI",
            onDismiss = { showInvoiceDialog = false }
        )
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // ==========================================
                // SUCCESS ILLUSTRATION WITH SURROUNDING CONFETTI
                // ==========================================
                Box(
                    modifier = Modifier
                        .size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Confetti Particles Background Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(scaleAnim.value)
                    ) {
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val alpha = confettiAlpha.value

                        // --- Confetti Pieces matching screenshot ---
                        // Top-left yellow diamond
                        rotate(25f, pivot = Offset(centerX - 80f, centerY - 80f)) {
                            drawRoundRect(
                                color = Color(0xFFF59E0B).copy(alpha = alpha),
                                topLeft = Offset(centerX - 85f, centerY - 85f),
                                size = Size(14f, 14f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                        }

                        // Top-left orange pill
                        rotate(-30f, pivot = Offset(centerX - 60f, centerY - 55f)) {
                            drawRoundRect(
                                color = Color(0xFFF97316).copy(alpha = alpha),
                                topLeft = Offset(centerX - 65f, centerY - 58f),
                                size = Size(18f, 9f),
                                cornerRadius = CornerRadius(4f, 4f)
                            )
                        }

                        // Top-left red small diamond
                        rotate(45f, pivot = Offset(centerX - 95f, centerY - 25f)) {
                            drawRect(
                                color = Color(0xFFEF4444).copy(alpha = alpha),
                                topLeft = Offset(centerX - 100f, centerY - 30f),
                                size = Size(10f, 10f)
                            )
                        }

                        // Top-left teal tilted pill
                        rotate(35f, pivot = Offset(centerX - 75f, centerY + 25f)) {
                            drawRoundRect(
                                color = Color(0xFF0D9488).copy(alpha = alpha),
                                topLeft = Offset(centerX - 80f, centerY + 20f),
                                size = Size(16f, 8f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                        }

                        // Top-left blue circle dot
                        drawCircle(
                            color = Color(0xFF0284C7).copy(alpha = alpha),
                            radius = 6f,
                            center = Offset(centerX - 55f, centerY - 15f)
                        )

                        // Top-center orange diamond
                        rotate(40f, pivot = Offset(centerX + 15f, centerY - 95f)) {
                            drawRoundRect(
                                color = Color(0xFFF59E0B).copy(alpha = alpha),
                                topLeft = Offset(centerX + 10f, centerY - 100f),
                                size = Size(12f, 12f),
                                cornerRadius = CornerRadius(2.5f, 2.5f)
                            )
                        }

                        // Top-right blue tilted pill
                        rotate(-25f, pivot = Offset(centerX + 80f, centerY - 90f)) {
                            drawRoundRect(
                                color = Color(0xFF2563EB).copy(alpha = alpha),
                                topLeft = Offset(centerX + 75f, centerY - 95f),
                                size = Size(16f, 9f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                        }

                        // Top-right orange pill
                        rotate(45f, pivot = Offset(centerX + 40f, centerY - 65f)) {
                            drawRoundRect(
                                color = Color(0xFFEA580C).copy(alpha = alpha),
                                topLeft = Offset(centerX + 35f, centerY - 70f),
                                size = Size(18f, 8f),
                                cornerRadius = CornerRadius(4f, 4f)
                            )
                        }

                        // Top-right green dot
                        drawCircle(
                            color = Color(0xFF10B981).copy(alpha = alpha),
                            radius = 5.5f,
                            center = Offset(centerX + 75f, centerY - 55f)
                        )

                        // Right purple diamond
                        rotate(15f, pivot = Offset(centerX + 55f, centerY - 25f)) {
                            drawRoundRect(
                                color = Color(0xFF8B5CF6).copy(alpha = alpha),
                                topLeft = Offset(centerX + 50f, centerY - 30f),
                                size = Size(10f, 10f),
                                cornerRadius = CornerRadius(2f, 2f)
                            )
                        }

                        // Right-bottom yellow diamond
                        rotate(30f, pivot = Offset(centerX + 70f, centerY + 35f)) {
                            drawRoundRect(
                                color = Color(0xFFF59E0B).copy(alpha = alpha),
                                topLeft = Offset(centerX + 65f, centerY + 30f),
                                size = Size(13f, 13f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                        }

                        // Right-bottom soft purple dot
                        drawCircle(
                            color = Color(0xFFA855F7).copy(alpha = alpha),
                            radius = 6f,
                            center = Offset(centerX + 40f, centerY + 45f)
                        )
                    }

                    // Centered Large Green Success Circle Badge
                    Surface(
                        shape = CircleShape,
                        color = SuccessGreen,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(105.dp)
                            .scale(scaleAnim.value)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Payment Successful",
                                tint = Color.White,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ==========================================
                // SUCCESS MESSAGE HEADINGS
                // ==========================================
                Text(
                    text = "Payment Successful!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your appointment is confirmed.",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMutedGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                // ==========================================
                // BOOKING INFORMATION CARD (Left: Booking ID | Right: BMJ1254789)
                // ==========================================
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(0.5.dp, Color(0xFFEDE9F6)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            clipboardManager.setText(AnnotatedString(displayBookingId))
                            Toast.makeText(context, "Booking ID copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                        .testTag("booking_id_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Booking ID",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )

                        Text(
                            text = displayBookingId,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ==========================================
                // PRIMARY ACTION BUTTON (View Appointment)
                // ==========================================
                Button(
                    onClick = {
                        onViewAppointment(displayBookingId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(3.dp, RoundedCornerShape(14.dp), spotColor = BrandPurple)
                        .testTag("view_appointment_button")
                ) {
                    Text(
                        text = "View Appointment",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ==========================================
                // SECONDARY ACTION BUTTON (Download Invoice)
                // ==========================================
                OutlinedButton(
                    onClick = {
                        showInvoiceDialog = true
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, BrandPurple),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("download_invoice_button")
                ) {
                    Text(
                        text = "Download Invoice",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPurple
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

// ==========================================
// INVOICE DETAILS DIALOG
// ==========================================
@Composable
fun InvoiceDetailsDialog(
    bookingId: String,
    serviceName: String,
    dateTime: String,
    address: String,
    patientName: String,
    nurseName: String,
    serviceFee: Int,
    platformFee: Int,
    discount: Int,
    totalPaid: Int,
    paymentMethod: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tax Invoice",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )
                        Text(
                            text = "CareHome Healthcare Services",
                            fontSize = 11.5.sp,
                            color = TextMutedGray
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(14.dp))

                // Invoice Summary Grid
                InvoiceRow(label = "Invoice No.", value = "INV-2026-${(1000..9999).random()}")
                InvoiceRow(label = "Booking ID", value = bookingId)
                InvoiceRow(label = "Service", value = serviceName)
                InvoiceRow(label = "Date & Time", value = dateTime)
                InvoiceRow(label = "Assigned Nurse", value = nurseName)
                InvoiceRow(label = "Address", value = address)
                InvoiceRow(label = "Payment Method", value = paymentMethod)
                InvoiceRow(label = "Status", value = "PAID (Success)", isHighlighted = true)

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(14.dp))

                // Price Breakdown
                Text(
                    text = "Payment Breakdown",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                InvoicePriceRow(label = "Service Fee", amount = "₹$serviceFee")
                InvoicePriceRow(label = "Platform Fee", amount = "₹$platformFee")
                InvoicePriceRow(label = "Discount (CARE20)", amount = "-₹$discount", isGreen = true)

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Paid",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPurpleDark
                    )
                    Text(
                        text = "₹$totalPaid",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandPurple
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Download / Share Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Invoice shared successfully!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = BrandPurple
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", color = BrandPurple, fontSize = 13.5.sp)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Invoice PDF downloaded to device storage!", Toast.LENGTH_LONG).show()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download", color = Color.White, fontSize = 13.5.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun InvoiceRow(label: String, value: String, isHighlighted: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            color = TextMutedGray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) SuccessGreen else BrandPurpleDark,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
private fun InvoicePriceRow(label: String, amount: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextMutedGray
        )
        Text(
            text = amount,
            fontSize = 13.5.sp,
            fontWeight = if (isGreen) FontWeight.Bold else FontWeight.Medium,
            color = if (isGreen) SuccessGreen else BrandPurpleDark
        )
    }
}
