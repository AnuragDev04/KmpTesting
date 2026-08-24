package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ServiceCategory
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

data class AllServiceDisplayItem(
    val id: String,
    val name: String,
    val description: String,
    val category: ServiceCategory,
    val iconType: ServiceIconType,
    val containerColor: Color,
    val iconColor: Color
)

enum class ServiceIconType {
    HOME_NURSING,
    VACCINATION,
    PATIENT_CARE,
    ELDER_CARE,
    MEDICAL_ASSISTANCE,
    PHYSIOTHERAPY,
    LAB_TEST,
    EMERGENCY_CARE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllServicesScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onServiceClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val allServicesList = remember {
        listOf(
            AllServiceDisplayItem(
                id = "NURSING_1",
                name = "Home Nursing",
                description = "Professional nursing care at your home",
                category = ServiceCategory.HOME_NURSING,
                iconType = ServiceIconType.HOME_NURSING,
                containerColor = Color(0xFFFFF5EC), // Soft warm cream/peach
                iconColor = Color(0xFFE65100)       // Orange accent
            ),
            AllServiceDisplayItem(
                id = "VAC_1",
                name = "Vaccination",
                description = "All age group vaccines at home",
                category = ServiceCategory.VACCINATION,
                iconType = ServiceIconType.VACCINATION,
                containerColor = Color(0xFFF4EEFD), // Soft lilac/lavender
                iconColor = Color(0xFF7C3AED)       // Purple accent
            ),
            AllServiceDisplayItem(
                id = "PATIENT_1",
                name = "Patient Care",
                description = "Post surgery & chronic care at home",
                category = ServiceCategory.PATIENT_CARE,
                iconType = ServiceIconType.PATIENT_CARE,
                containerColor = Color(0xFFF1F0FD), // Soft lavender
                iconColor = Color(0xFF6366F1)       // Indigo/purple accent
            ),
            AllServiceDisplayItem(
                id = "ELDER_1",
                name = "Elder Care",
                description = "Compassionate care for your loved ones",
                category = ServiceCategory.ELDER_CARE,
                iconType = ServiceIconType.ELDER_CARE,
                containerColor = Color(0xFFF3EDFD), // Soft violet/lavender
                iconColor = Color(0xFF7C3AED)       // Purple accent
            ),
            AllServiceDisplayItem(
                id = "MED_1",
                name = "Medical Assistance",
                description = "Doctor consultation at home",
                category = ServiceCategory.MEDICAL_ASSISTANCE,
                iconType = ServiceIconType.MEDICAL_ASSISTANCE,
                containerColor = Color(0xFFF1F0F8), // Soft slate/purple
                iconColor = Color(0xFF6366F1)       // Indigo/purple accent
            ),
            AllServiceDisplayItem(
                id = "PHYSIO_1",
                name = "Physiotherapy",
                description = "Rehabilitation & pain management",
                category = ServiceCategory.PHYSIOTHERAPY,
                iconType = ServiceIconType.PHYSIOTHERAPY,
                containerColor = Color(0xFFF1EEFA), // Soft lavender
                iconColor = Color(0xFF7C3AED)       // Purple accent
            ),
            AllServiceDisplayItem(
                id = "LAB_1",
                name = "Lab Test",
                description = "Sample collection at home",
                category = ServiceCategory.LAB_TEST,
                iconType = ServiceIconType.LAB_TEST,
                containerColor = Color(0xFFF3EEFC), // Soft lavender
                iconColor = Color(0xFF7C3AED)       // Purple accent
            ),
            AllServiceDisplayItem(
                id = "EMERGENCY_1",
                name = "Emergency Care",
                description = "Immediate care & assistance",
                category = ServiceCategory.EMERGENCY_CARE,
                iconType = ServiceIconType.EMERGENCY_CARE,
                containerColor = Color(0xFFFFF0EE), // Soft peach/pink
                iconColor = Color(0xFFEF4444)       // Red/coral accent
            )
        )
    }

    val filteredServices = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allServicesList
        } else {
            val query = searchQuery.trim().lowercase()
            allServicesList.filter {
                it.name.lowercase().contains(query) ||
                it.description.lowercase().contains(query)
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFFDFBFD), // Ultra soft clean off-white canvas
        topBar = {
            // ==========================================
            // COMPACT TOP HEADER (Matching Reference)
            // ==========================================
            Surface(
                color = Color.White,
                shadowElevation = 0.5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    // Back Arrow on Left
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .testTag("all_services_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back to Home",
                            tint = Color(0xFF1E1B4B),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Centered Title
                    Text(
                        text = "All Services",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1B4B),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SEARCH BAR (Directly Below Header)
            // ==========================================
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search services...",
                        fontSize = 14.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = PurplePrimary.copy(alpha = 0.6f),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    cursorColor = PurplePrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("all_services_search_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // SERVICES LIST (Vertical Scrollable)
            // ==========================================
            if (filteredServices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No services found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try searching for another healthcare service",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = filteredServices,
                        key = { it.id }
                    ) { item ->
                        AllServiceRowCard(
                            item = item,
                            onClick = {
                                onServiceClick(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// ALL SERVICE ROW CARD (Matching Reference 1:1)
// ==========================================
@Composable
fun AllServiceRowCard(
    item: AllServiceDisplayItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("service_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Pastel Icon Container
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = item.containerColor,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ServiceItemGraphic(
                            iconType = item.iconType,
                            tint = item.iconColor
                        )
                    }
                }

                // Text Information
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = item.description,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF64748B),
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Light Chevron Right Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open ${item.name}",
                tint = Color(0xFFCBD5E1),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ==========================================
// PIXEL-PERFECT GRAPHIC ICONS
// ==========================================
@Composable
fun ServiceItemGraphic(
    iconType: ServiceIconType,
    tint: Color,
    modifier: Modifier = Modifier
) {
    when (iconType) {
        ServiceIconType.HOME_NURSING -> {
            // Home with cross inside
            Box(
                modifier = modifier.size(26.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(26.dp)
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(12.dp).offset(y = 2.dp)
                )
            }
        }
        ServiceIconType.VACCINATION -> {
            Icon(
                imageVector = Icons.Outlined.Vaccines,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.PATIENT_CARE -> {
            Icon(
                imageVector = Icons.Outlined.PersonalInjury,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.ELDER_CARE -> {
            Icon(
                imageVector = Icons.Outlined.Elderly,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.MEDICAL_ASSISTANCE -> {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.PHYSIOTHERAPY -> {
            Icon(
                imageVector = Icons.Outlined.AccessibilityNew,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.LAB_TEST -> {
            Icon(
                imageVector = Icons.Outlined.Science,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
        ServiceIconType.EMERGENCY_CARE -> {
            Icon(
                imageVector = Icons.Default.Emergency,
                contentDescription = null,
                tint = tint,
                modifier = modifier.size(26.dp)
            )
        }
    }
}
