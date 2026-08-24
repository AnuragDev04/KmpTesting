package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ServiceCategory
import com.example.ui.components.NearbyNurseCard
import com.example.ui.components.ServiceCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

data class QuickServiceItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val category: ServiceCategory,
    val isEmergency: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onServiceClick: (String) -> Unit,
    onBookServiceClick: (String) -> Unit,
    onViewAllServicesClick: () -> Unit = {},
    onNurseClick: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAiAdvisor: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    val context = LocalContext.current
    val loggedUser by viewModel.loggedInUser.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsState()

    var selectedCity by remember { mutableStateOf("Bangalore") }
    var showCityDialog by remember { mutableStateOf(false) }
    var showNotificationSheet by remember { mutableStateOf(false) }
    var showAllServicesDialog by remember { mutableStateOf(false) }
    var showAllNursesDialog by remember { mutableStateOf(false) }
    var isListeningVoice by remember { mutableStateOf(false) }

    val userAvatarResId = remember {
        context.resources.getIdentifier("user_avatar_riya_1787034560071", "drawable", context.packageName)
    }

    val promoArtResId = remember {
        context.resources.getIdentifier("nurse_promo_art_1787034572378", "drawable", context.packageName)
    }

    val quickServices = remember {
        listOf(
            QuickServiceItem(
                id = "NURSING_1",
                title = "Home\nNursing",
                icon = Icons.Outlined.HomeWork,
                category = ServiceCategory.HOME_NURSING
            ),
            QuickServiceItem(
                id = "VAC_1",
                title = "Vaccination",
                icon = Icons.Outlined.Vaccines,
                category = ServiceCategory.VACCINATION
            ),
            QuickServiceItem(
                id = "PATIENT_1",
                title = "Patient\nCare",
                icon = Icons.Outlined.PersonalInjury,
                category = ServiceCategory.PATIENT_CARE
            ),
            QuickServiceItem(
                id = "ELDER_1",
                title = "Elder\nCare",
                icon = Icons.Outlined.Elderly,
                category = ServiceCategory.ELDER_CARE
            ),
            QuickServiceItem(
                id = "MED_1",
                title = "Medical\nAssistance",
                icon = Icons.Outlined.MonitorHeart,
                category = ServiceCategory.MEDICAL_ASSISTANCE
            ),
            QuickServiceItem(
                id = "PHYSIO_1",
                title = "Physiotherapy",
                icon = Icons.Outlined.AccessibilityNew,
                category = ServiceCategory.PHYSIOTHERAPY
            ),
            QuickServiceItem(
                id = "LAB_1",
                title = "Lab Test",
                icon = Icons.Outlined.Science,
                category = ServiceCategory.LAB_TEST
            ),
            QuickServiceItem(
                id = "EMERGENCY_1",
                title = "Emergency\nCare",
                icon = Icons.Outlined.Shield,
                category = ServiceCategory.EMERGENCY_CARE,
                isEmergency = true
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // ==========================================
            // 1. TOP APP HEADER (Location + Bell + Avatar)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Location Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showCityDialog = true }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = PurplePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedCity,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Location",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Right Actions: Notification Bell + Profile Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Notification Bell with badge indicator
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable { onNavigateToNotifications() }
                            .testTag("home_notification_bell_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        // Notification badge counter / dot
                        if (unreadNotifsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(if (unreadNotifsCount > 9) 16.dp else 14.dp)
                                    .background(OrangeAccent, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-2).dp, y = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (unreadNotifsCount > 9) "9+" else "$unreadNotifsCount",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // User Profile Circular Image
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, PurpleBorder, CircleShape)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (userAvatarResId != 0) {
                            Image(
                                painter = painterResource(id = userAvatarResId),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = PurplePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 2. GREETING & USER NAME
            // ==========================================
            Column {
                Text(
                    text = "Good Morning,",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                val userName = loggedUser?.name ?: "Riya Sharma"
                Text(
                    text = "$userName 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 3. ROUNDED SEARCH BAR
            // ==========================================
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search services, doctors...",
                        fontSize = 14.sp,
                        color = TextMuted
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
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search",
                                tint = TextSecondary
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            isListeningVoice = !isListeningVoice
                            if (isListeningVoice) {
                                Toast.makeText(context, "Listening... Say 'Home Nurse' or 'Physiotherapy'", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Search",
                                tint = PurplePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedBorderColor = PurplePrimary.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    cursorColor = PurplePrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            // If user is searching, show live filtered results immediately
            if (searchQuery.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Results (${viewModel.filteredServices.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    TextButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Text("Clear", color = PurplePrimary, fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    viewModel.filteredServices.forEach { service ->
                        ServiceCard(
                            service = service,
                            onClick = { onServiceClick(service.id) },
                            onBookNow = { onBookServiceClick(service.id) }
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(22.dp))

                // ==========================================
                // 4. QUICK SERVICES SECTION (Header + 2x4 Grid)
                // ==========================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Services",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurplePrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onViewAllServicesClick() }
                            .padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2 Rows of 4 Services (Grid Layout)
                val row1 = quickServices.take(4)
                val row2 = quickServices.drop(4).take(4)

                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row1.forEach { item ->
                        QuickServiceButton(
                            item = item,
                            onClick = {
                                val matching = viewModel.services.firstOrNull { it.category == item.category }
                                if (matching != null) {
                                    onServiceClick(matching.id)
                                } else {
                                    onServiceClick(item.id)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row2.forEach { item ->
                        QuickServiceButton(
                            item = item,
                            onClick = {
                                val matching = viewModel.services.firstOrNull { it.category == item.category }
                                if (matching != null) {
                                    onServiceClick(matching.id)
                                } else {
                                    onServiceClick(item.id)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // 5. PROMOTIONAL BANNER
                // ==========================================
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.applyCoupon("CARE20")
                            Toast.makeText(context, "Promo Code CARE20 Applied! (20% OFF)", Toast.LENGTH_SHORT).show()
                            onBookServiceClick("NURSING_1")
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(138.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF2E1065), // Deep Rich Violet
                                        Color(0xFF4C1D95), // Indigo Purple
                                        Color(0xFF5B21B6), // Primary Purple
                                        Color(0xFF6D28D9)  // Medium Violet
                                    )
                                )
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left Text & Coupon Badge
                            Column(
                                modifier = Modifier.weight(1.3f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Flat 20% OFF",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "On your first booking",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                // Coupon Code Pill
                                Surface(
                                    color = Color(0x3D000000),
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4DFFFFFF))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Use Code: ",
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                        Text(
                                            text = "CARE20",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // Right Nurse Illustration Asset
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (promoArtResId != 0) {
                                    Image(
                                        painter = painterResource(id = promoArtResId),
                                        contentDescription = "Nurse Promotional Illustration",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.HealthAndSafety,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(72.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // 6. NEARBY NURSES SECTION
                // ==========================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Nurses",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurplePrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { showAllNursesDialog = true }
                            .padding(vertical = 4.dp, horizontal = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nurse Card (Priya Sharma matching reference)
                val primaryNurse = viewModel.nurses.firstOrNull() ?: com.example.data.models.Nurse(
                    id = "NURSE_101",
                    name = "Priya Sharma",
                    qualification = "B.Sc Nursing",
                    experienceYears = 5,
                    rating = 4.8,
                    reviewsCount = 120,
                    phone = "+91 98765 43210",
                    specialization = "General Nursing",
                    bio = "5 years experience in Bangalore",
                    completedVisits = 240
                )

                NearbyNurseCard(
                    nurse = primaryNurse,
                    distanceText = "1.2 km away",
                    onClick = {
                        onNurseClick(primaryNurse.id)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Emergency SOS floating card below for quick triage
                Surface(
                    color = Color(0xFFFEF2F2),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEE2E2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+18002273466"))
                            context.startActivity(intent)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(StatusRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "24/7 Critical Emergency Helpline",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusRed
                                )
                                Text(
                                    text = "Tap to call on-call doctor & ICU nurse triage",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = StatusRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // Extra padding for bottom navigation
            }
        }
    }

    // --- City / Locality Selection Dialog ---
    if (showCityDialog) {
        val localities = listOf("Bangalore", "Indiranagar, Bangalore", "Koramangala, Bangalore", "HSR Layout, Bangalore", "Whitefield, Bangalore", "Jayanagar, Bangalore")
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = {
                Text(
                    text = "Select Service Location",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    localities.forEach { loc ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedCity == loc) PurpleLightContainer else Color(0xFFF8FAFC),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCity = loc
                                    showCityDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = if (selectedCity == loc) PurplePrimary else TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = loc,
                                        fontSize = 14.sp,
                                        fontWeight = if (selectedCity == loc) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedCity == loc) PurplePrimary else TextPrimary
                                    )
                                }
                                if (selectedCity == loc) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PurplePrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Close", color = PurplePrimary)
                }
            }
        )
    }

    // --- Notifications Dialog ---
    if (showNotificationSheet) {
        AlertDialog(
            onDismissRequest = { showNotificationSheet = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = PurplePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PurpleLightContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocalOffer, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("New Promo: Flat 20% OFF", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text("Use code CARE20 on your first booking with top verified nurses.", fontSize = 11.5.sp, color = TextSecondary)
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Sister Priya Sharma is nearby", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text("Available for immediate home care in Indiranagar, Bangalore.", fontSize = 11.5.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotificationSheet = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Got it")
                }
            }
        )
    }

    // --- View All Services Dialog ---
    if (showAllServicesDialog) {
        AlertDialog(
            onDismissRequest = { showAllServicesDialog = false },
            title = {
                Text("All Healthcare Services", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    viewModel.services.forEach { service ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9FAFB),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showAllServicesDialog = false
                                    onServiceClick(service.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(service.name, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TextPrimary)
                                    Text("₹${service.price.toInt()} • ${service.duration}", fontSize = 11.5.sp, color = PurplePrimary)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAllServicesDialog = false }) {
                    Text("Close", color = PurplePrimary)
                }
            }
        )
    }

    // --- View All Nurses Dialog ---
    if (showAllNursesDialog) {
        AlertDialog(
            onDismissRequest = { showAllNursesDialog = false },
            title = {
                Text("Verified Nearby Nurses", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    viewModel.nurses.forEach { nurse ->
                        NearbyNurseCard(
                            nurse = nurse,
                            distanceText = "${(1.0 + (nurse.experienceYears % 3) * 0.4)} km away",
                            onClick = {
                                showAllNursesDialog = false
                                onNurseClick(nurse.id)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAllNursesDialog = false }) {
                    Text("Close", color = PurplePrimary)
                }
            }
        )
    }
}

// ==========================================
// QUICK SERVICE BUTTON COMPONENT (Reference 1:1)
// ==========================================
@Composable
private fun QuickServiceButton(
    item: QuickServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 3.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Rounded Soft Icon Container
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (item.isEmergency) Color(0xFFFFF1F2) else Color(0xFFF5F3FF),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (item.isEmergency) Color(0xFFFECDD3) else Color(0xFFEDE9FE)
            ),
            modifier = Modifier.size(60.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title.replace("\n", " "),
                    tint = if (item.isEmergency) Color(0xFFE11D48) else PurplePrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Service Title Label
        Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2
        )
    }
}
