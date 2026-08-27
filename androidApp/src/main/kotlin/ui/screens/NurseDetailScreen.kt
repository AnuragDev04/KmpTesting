package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.models.Nurse
import com.example.ui.viewmodel.MainViewModel

// Custom Color Palette strictly matching Nurse Profile Reference (Screenshot 2026-08-18 152902.png)
private val HeroBgLavender = Color(0xFFEBE6F8)       // Soft pastel clinic lavender hero background
private val DarkPurpleText = Color(0xFF1E153A)       // Primary dark navy/purple text
private val MutedSlateText = Color(0xFF64748B)       // Muted gray/slate secondary text
private val StarAmber = Color(0xFFF59E0B)            // Orange/Amber star rating
private val BrandPurple = Color(0xFF5E3A8C)          // Deep vibrant purple for Call button & outlines
private val ChipBgPurple = Color(0xFFF3EAF8)         // Very light lavender pill container
private val CardBorderLight = Color(0xFFF1EBF7)      // Light card borders

@Composable
fun NurseDetailScreen(
    nurseId: String,
    serviceId: String = "NURSING_1",
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onBookWithNurseClick: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val allNurses = viewModel.nurses
    val nurse = remember(nurseId, allNurses) {
        allNurses.firstOrNull { it.id.equals(nurseId, ignoreCase = true) }
            ?: allNurses.firstOrNull { it.name.contains(nurseId, ignoreCase = true) }
            ?: allNurses.first()
    }

    var isFavorite by remember { mutableStateOf(nurse.isFavorite) }
    var showChatDialog by remember { mutableStateOf(false) }

    // Resolve portrait image dynamically
    val avatarDrawableResName = nurse.imageDrawableResName ?: when {
        nurse.name.contains("priya", ignoreCase = true) -> "nurse_priya_hero_1787048302741"
        nurse.name.contains("ankit", ignoreCase = true) -> "nurse_ankit_pic_1787046592042"
        nurse.name.contains("neha", ignoreCase = true) -> "nurse_neha_pic_1787046614205"
        else -> "nurse_priya_hero_1787048302741"
    }

    val imageResId = remember(avatarDrawableResName) {
        val res = context.resources.getIdentifier(avatarDrawableResName, "drawable", context.packageName)
        if (res != 0) res else {
            context.resources.getIdentifier("nurse_priya_pic_1787034588602", "drawable", context.packageName)
        }
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM ACTION BAR (Call & Message)
            // ==========================================
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, CardBorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Call Button (Filled Purple with Phone Icon)
                    Button(
                        onClick = {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${nurse.phone}"))
                            context.startActivity(dialIntent)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("nurse_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Call",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Message Button (Outlined White with Purple Border)
                    OutlinedButton(
                        onClick = { showChatDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, BrandPurple),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("nurse_message_button")
                    ) {
                        Text(
                            text = "Message",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurple
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // ==========================================
                // TOP PROFILE IMAGE / HERO SECTION
                // ==========================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFE8E0F5),
                                    Color(0xFFF3EDFB),
                                    Color(0xFFFAF7FD)
                                )
                            )
                        )
                ) {
                    // Centered Nurse Portrait Image
                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = nurse.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 28.dp)
                        )
                    }

                    // Top Navigation Bar (Back Arrow + Favorite Heart)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("nurse_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = DarkPurpleText,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Favorite Heart Button (Circular White Container)
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    isFavorite = !isFavorite
                                    val msg = if (isFavorite) "Added ${nurse.name} to Favorites" else "Removed from Favorites"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                .testTag("nurse_favorite_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = BrandPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // WHITE NURSE INFORMATION SHEET
                // ==========================================
                Surface(
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Nurse Name, Profession & Rating
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = nurse.name,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkPurpleText
                            )

                            Text(
                                text = nurse.profession,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MutedSlateText
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = StarAmber,
                                    modifier = Modifier.size(17.dp)
                                )

                                Text(
                                    text = String.format("%.1f", nurse.rating),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkPurpleText
                                )

                                Text(
                                    text = "(${nurse.reviewsCount} Reviews)",
                                    fontSize = 13.sp,
                                    color = MutedSlateText
                                )
                            }
                        }

                        // ==========================================
                        // 3 NURSE STATISTICS CARDS
                        // ==========================================
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // 1. Years Experience Card
                            StatisticCard(
                                value = "${nurse.experienceYears}",
                                label = "Years Exp.",
                                modifier = Modifier.weight(1f)
                            )

                            // 2. Patients Served Card
                            StatisticCard(
                                value = nurse.patientsServed,
                                label = "Patients",
                                modifier = Modifier.weight(1f)
                            )

                            // 3. Satisfaction Card
                            StatisticCard(
                                value = "${nurse.satisfactionPercent}%",
                                label = "Satisfaction",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // ==========================================
                        // ABOUT SECTION
                        // ==========================================
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "About",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkPurpleText
                            )

                            Text(
                                text = nurse.bio,
                                fontSize = 13.5.sp,
                                color = Color(0xFF475569),
                                lineHeight = 19.sp
                            )
                        }

                        // ==========================================
                        // LANGUAGES SECTION
                        // ==========================================
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Languages",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkPurpleText
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                nurse.languages.forEach { language ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = ChipBgPurple,
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(horizontal = 14.dp)
                                        ) {
                                            Text(
                                                text = language,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = DarkPurpleText
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    // ==========================================
    // IN-APP CHAT MODAL DIALOG
    // ==========================================
    if (showChatDialog) {
        var chatInput by remember { mutableStateOf("") }
        val chatMessages = remember {
            mutableStateListOf(
                "Hello! I am ${nurse.name}. How can I assist you with your home healthcare requirements?",
                "I am available for verified home nursing visits, patient care, and vitals monitoring."
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
                            Surface(
                                shape = CircleShape,
                                color = ChipBgPurple,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = BrandPurple,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = nurse.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = DarkPurpleText
                                )
                                Text(
                                    text = "Online • ${nurse.profession}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF16A34A)
                                )
                            }
                        }

                        IconButton(
                            onClick = { showChatDialog = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MutedSlateText)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = CardBorderLight)

                    // Chat Messages List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ChipBgPurple,
                                modifier = Modifier.fillMaxWidth(0.88f)
                            ) {
                                Text(
                                    text = msg,
                                    fontSize = 12.5.sp,
                                    color = DarkPurpleText,
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
                            colors = IconButtonDefaults.iconButtonColors(containerColor = BrandPurple),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 1:1 REUSABLE STATISTIC CARD
// ==========================================
@Composable
private fun StatisticCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurpleText,
                textAlign = TextAlign.Center
            )

            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = MutedSlateText,
                textAlign = TextAlign.Center
            )
        }
    }
}
