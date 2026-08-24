package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val tickets by viewModel.allSupportTickets.collectAsState()

    var subjectInput by remember { mutableStateOf("") }
    var messageInput by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val faqs = listOf(
        "How are your home nurses verified?" to "All nurses undergo strict medical qualification checks (B.Sc / GNM degree verification), police criminal background verification, and mandatory hygiene protocols.",
        "Can I reschedule or cancel a booking?" to "Yes, you can easily reschedule or cancel an appointment anytime up to 2 hours before the scheduled time slot from your Appointment Details screen.",
        "What payment methods are accepted?" to "We support online payments via UPI (Google Pay, PhonePe, Paytm), Credit & Debit Cards, Net Banking, and Digital Wallets powered by Razorpay, Stripe, and PhonePe.",
        "What if I need emergency medical support?" to "For immediate life-threatening medical emergencies, tap the Emergency Call button to connect instantly with our 24/7 critical triage team or call 112."
    )

    var expandedFaqIndex by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Support & FAQs", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hotline Call Support Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TealPrimaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(TealPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("24/7 Helpline Support", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TealPrimaryDark)
                        Text("+1-800-CARE-HOME (+1-800-227-3466)", fontSize = 12.sp, color = TextSecondary)
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+18002273466"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Call Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // FAQ Accordion Section
            Column {
                Text(
                    text = "Frequently Asked Questions (FAQs)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                faqs.forEachIndexed { index, pair ->
                    val isExpanded = (expandedFaqIndex == index)
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { expandedFaqIndex = if (isExpanded) -1 else index }
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pair.first,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TealPrimary
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = pair.second,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Submit Support Ticket Form
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Submit a Support Ticket",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )

                    OutlinedTextField(
                        value = subjectInput,
                        onValueChange = { subjectInput = it },
                        label = { Text("Issue Subject") },
                        placeholder = { Text("e.g. Query regarding invoice or nurse arrival") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        label = { Text("Detailed Description") },
                        placeholder = { Text("Describe how we can assist you...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (subjectInput.isNotBlank() && messageInput.isNotBlank()) {
                                isSubmitting = true
                                viewModel.submitSupportTicket(subjectInput, messageInput) {
                                    isSubmitting = false
                                    subjectInput = ""
                                    messageInput = ""
                                    Toast.makeText(context, "Support ticket submitted successfully!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Submit Ticket", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Ticket History Section
            if (tickets.isNotEmpty()) {
                Column {
                    Text(
                        text = "Your Submitted Tickets",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    tickets.forEach { ticket ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(ticket.subject, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Surface(color = TealPrimary, shape = RoundedCornerShape(6.dp)) {
                                        Text(ticket.status, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text(ticket.message, fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
