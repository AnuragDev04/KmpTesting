package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppointmentStatus
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseTrackingScreen(
    appointmentId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val appointments by viewModel.allAppointments.collectAsState()
    val appointment = appointments.firstOrNull { it.id == appointmentId }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Nurse Tracking", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Simulated Interactive Map Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Grid road lines
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, height * 0.3f),
                        end = Offset(width, height * 0.3f),
                        strokeWidth = 14f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(width * 0.4f, 0f),
                        end = Offset(width * 0.4f, height),
                        strokeWidth = 14f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, height * 0.7f),
                        end = Offset(width, height * 0.7f),
                        strokeWidth = 10f
                    )

                    // Route dash line from Nurse to Destination
                    drawPath(
                        path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(width * 0.2f, height * 0.3f)
                            lineTo(width * 0.4f, height * 0.3f)
                            lineTo(width * 0.4f, height * 0.7f)
                            lineTo(width * 0.75f, height * 0.7f)
                        },
                        color = TealPrimary,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 6f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                    )

                    // Nurse Pulsing Radar Location
                    drawCircle(
                        color = TealPrimary.copy(alpha = 0.3f),
                        radius = pulseRadius,
                        center = Offset(width * 0.2f, height * 0.3f)
                    )
                    drawCircle(
                        color = TealPrimary,
                        radius = 16f,
                        center = Offset(width * 0.2f, height * 0.3f)
                    )

                    // Patient Home Pin
                    drawCircle(
                        color = StatusRed,
                        radius = 18f,
                        center = Offset(width * 0.75f, height * 0.7f)
                    )
                }

                // ETA Overlay Chip
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Estimated Arrival: 14 Mins",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                            Text(
                                text = "Distance: 3.2 km away • Live Route",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Nurse Profile Summary Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(TealPrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = appointment?.assignedNurseName ?: "Sister Priya Sharma",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = StatusGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = appointment?.assignedNurseQualification ?: "B.Sc ICU Trained Nurse",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )

                                StarRatingBar(rating = appointment?.assignedNurseRating ?: 4.9, reviewsCount = 210)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val phone = appointment?.assignedNursePhone ?: "+919876543210"
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call Nurse Direct")
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+18002273466"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Support Desk")
                            }
                        }
                    }
                }

                // Pre-Visit Preparation Checklist
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "📋 Pre-Visit Checklist for Patient Home",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )

                        val checklist = listOf(
                            "Keep Doctor's prescriptions and past discharge summary accessible.",
                            "Ensure the patient room is clean, well-lit, and ventilated.",
                            "Keep a clean towel and glass of drinking water ready.",
                            "Inform nurse about any recent drug allergies or sudden fever spikes."
                        )

                        checklist.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
