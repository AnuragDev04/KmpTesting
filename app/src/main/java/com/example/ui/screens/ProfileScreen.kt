package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

// Palette matching Reference (Screenshot 2026-08-18 161219.png)
private val ScreenBg = Color(0xFFF9FAFB)            // Light canvas background
private val TextDarkNavy = Color(0xFF1E153A)        // Deep dark navy / purple for Name and Menu titles
private val TextMutedSlate = Color(0xFF64748B)      // Slate gray for phone and subtitles
private val MenuIconTint = Color(0xFF4338CA)        // Deep purple / indigo outline for menu icons
private val ChevronTint = Color(0xFF4338CA)         // Matching purple tint for right chevrons
private val CardBorderColor = Color(0xFFF1F5F9)     // Delicate outer container border
private val LogoutRed = Color(0xFFEF4444)           // Red accent for Logout button & icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit = {},
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToAiAdvisor: () -> Unit = {},
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current
    val user by viewModel.loggedInUser.collectAsState()

    // Modals / Sheet States
    var showPersonalInfoDialog by remember { mutableStateOf(false) }
    var showMedicalRecordsDialog by remember { mutableStateOf(false) }
    var showSavedAddressesDialog by remember { mutableStateOf(false) }
    var showPaymentMethodsDialog by remember { mutableStateOf(false) }
    var showHelpSupportDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    // User Avatar Resource lookup
    val userAvatarResId = remember {
        context.resources.getIdentifier("user_avatar_riya_1787034560071", "drawable", context.packageName)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg),
        containerColor = ScreenBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            // ==========================================
            // MAIN WHITE PROFILE CONTAINER (1:1 Reference)
            // ==========================================
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0x0A000000),
                        spotColor = Color(0x0F000000)
                    )
                    .testTag("profile_container_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 26.dp)
                ) {
                    // ------------------------------------------
                    // USER PROFILE HEADER
                    // ------------------------------------------
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_header_row"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular Profile Image (approx 72dp diameter)
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                                .background(Color(0xFFEDE9FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (userAvatarResId != 0) {
                                Image(
                                    painter = painterResource(id = userAvatarResId),
                                    contentDescription = "Riya Sharma Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = MenuIconTint,
                                    modifier = Modifier.size(38.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        // User Information
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = user?.name?.ifBlank { "Riya Sharma" } ?: "Riya Sharma",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkNavy,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = user?.phone?.ifBlank { "+91 98765 43210" } ?: "+91 98765 43210",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextMutedSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // ------------------------------------------
                    // PROFILE MENU ITEMS (Vertical list with generous spacing)
                    // ------------------------------------------
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        // 1. Personal Information
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.Person,
                            title = "Personal Information",
                            testTag = "menu_personal_information",
                            onClick = { showPersonalInfoDialog = true }
                        )

                        // 2. Medical Records
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.Assignment,
                            title = "Medical Records",
                            testTag = "menu_medical_records",
                            onClick = { showMedicalRecordsDialog = true }
                        )

                        // 3. Saved Addresses
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.LocationOn,
                            title = "Saved Addresses",
                            testTag = "menu_saved_addresses",
                            onClick = { showSavedAddressesDialog = true }
                        )

                        // 4. Payment Methods
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.AccountBalanceWallet,
                            title = "Payment Methods",
                            testTag = "menu_payment_methods",
                            onClick = { showPaymentMethodsDialog = true }
                        )

                        // 5. Help & Support
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.HeadsetMic,
                            title = "Help & Support",
                            testTag = "menu_help_support",
                            onClick = { showHelpSupportDialog = true }
                        )

                        // 6. Settings
                        ProfileMenuItemRow(
                            icon = Icons.Outlined.Settings,
                            title = "Settings",
                            testTag = "menu_settings",
                            onClick = { showSettingsDialog = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // ------------------------------------------
                    // LOGOUT BUTTON (1:1 Outlined red button)
                    // ------------------------------------------
                    OutlinedButton(
                        onClick = { showLogoutConfirmDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.2.dp, LogoutRed),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("logout_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                tint = LogoutRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = LogoutRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // ==========================================
    // LOGOUT CONFIRMATION DIALOG
    // ==========================================
    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = Color.White,
            icon = {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEE2E2),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = LogoutRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "Logout?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkNavy
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout from your account?",
                    fontSize = 14.sp,
                    color = TextMutedSlate
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        viewModel.logout()
                        onLogoutSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutConfirmDialog = false }
                ) {
                    Text("Cancel", color = TextMutedSlate)
                }
            }
        )
    }

    // ==========================================
    // 1. PERSONAL INFORMATION MODAL / SHEET
    // ==========================================
    if (showPersonalInfoDialog) {
        var editName by remember { mutableStateOf(user?.name ?: "Riya Sharma") }
        var editPhone by remember { mutableStateOf(user?.phone ?: "+91 98765 43210") }
        var editEmail by remember { mutableStateOf(user?.email ?: "riya.sharma@example.com") }
        var editGender by remember { mutableStateOf("Female") }
        var editDob by remember { mutableStateOf("14 August 1994") }
        var editBloodGroup by remember { mutableStateOf(user?.bloodGroup ?: "O+") }
        var editEmergency by remember { mutableStateOf(user?.emergencyContact ?: "+91 98112 23344") }

        AlertDialog(
            onDismissRequest = { showPersonalInfoDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Personal Information", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showPersonalInfoDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        readOnly = true
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editDob,
                            onValueChange = { editDob = it },
                            label = { Text("Date of Birth") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = editGender,
                            onValueChange = { editGender = it },
                            label = { Text("Gender") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editBloodGroup,
                            onValueChange = { editBloodGroup = it },
                            label = { Text("Blood Group") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = editEmergency,
                            onValueChange = { editEmergency = it },
                            label = { Text("Emergency Contact") },
                            modifier = Modifier.weight(1.2f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfile(
                            name = editName,
                            email = editEmail,
                            address = user?.address ?: "Flat 402, Green Valley, Indiranagar, Bangalore",
                            emergencyContact = editEmergency,
                            bloodGroup = editBloodGroup
                        )
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        showPersonalInfoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MenuIconTint),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ==========================================
    // 2. MEDICAL RECORDS MODAL
    // ==========================================
    if (showMedicalRecordsDialog) {
        AlertDialog(
            onDismissRequest = { showMedicalRecordsDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Medical Records", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showMedicalRecordsDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val records = listOf(
                        Triple("Post-Op Care Plan.pdf", "18 May 2026 • Dr. Rao", "Verified"),
                        Triple("CBC & Lipid Profile.pdf", "10 Apr 2026 • SRL Labs", "Verified"),
                        Triple("Vaccination Certificate.pdf", "15 Jan 2026 • Apollo Health", "Active")
                    )

                    records.forEach { (fileName, meta, status) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = MenuIconTint,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Column {
                                        Text(fileName, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                                        Text(meta, fontSize = 11.5.sp, color = TextMutedSlate)
                                    }
                                }
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = status,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF16A34A),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "File upload simulated: Record attached", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MenuIconTint)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = MenuIconTint)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Upload New Document", color = MenuIconTint, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMedicalRecordsDialog = false }) {
                    Text("Close", color = MenuIconTint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ==========================================
    // 3. SAVED ADDRESSES MODAL
    // ==========================================
    if (showSavedAddressesDialog) {
        AlertDialog(
            onDismissRequest = { showSavedAddressesDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Saved Addresses", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showSavedAddressesDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val addresses = listOf(
                        Pair("Home", user?.address ?: "Flat 402, Green Valley, 12th Main, Indiranagar, Bangalore - 560038"),
                        Pair("Office", "Tower B, Level 4, Embassy Tech Village, Outer Ring Road, Bangalore - 560103"),
                        Pair("Parents", "15, 4th Cross, Malleshwaram West, Bangalore - 560055")
                    )

                    addresses.forEach { (label, addr) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (label == "Home") Icons.Default.Home else if (label == "Office") Icons.Default.Work else Icons.Default.Place,
                                    contentDescription = null,
                                    tint = MenuIconTint,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(addr, fontSize = 12.sp, color = TextMutedSlate, lineHeight = 16.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "New Address dialog opened", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MenuIconTint)
                    ) {
                        Icon(Icons.Default.AddLocationAlt, contentDescription = null, tint = MenuIconTint)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Add New Address", color = MenuIconTint, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedAddressesDialog = false }) {
                    Text("Done", color = MenuIconTint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ==========================================
    // 4. PAYMENT METHODS MODAL
    // ==========================================
    if (showPaymentMethodsDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentMethodsDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Payment Methods", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showPaymentMethodsDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val paymentList = listOf(
                        Triple("Google Pay / UPI", "riya.sharma@oksbi", "Primary"),
                        Triple("HDFC Bank Debit Card", "•••• •••• •••• 4022", "Saved"),
                        Triple("Paytm Wallet", "Linked to +91 98765 43210", "Active")
                    )

                    paymentList.forEach { (name, desc, badge) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CreditCard,
                                        contentDescription = null,
                                        tint = MenuIconTint,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Column {
                                        Text(name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                                        Text(desc, fontSize = 11.5.sp, color = TextMutedSlate)
                                    }
                                }
                                Surface(
                                    color = Color(0xFFEDE9FE),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = badge,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MenuIconTint,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Add Card / UPI flow initiated", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MenuIconTint)
                    ) {
                        Icon(Icons.Default.AddCard, contentDescription = null, tint = MenuIconTint)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Add Payment Method", color = MenuIconTint, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaymentMethodsDialog = false }) {
                    Text("Done", color = MenuIconTint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ==========================================
    // 5. HELP & SUPPORT MODAL
    // ==========================================
    if (showHelpSupportDialog) {
        AlertDialog(
            onDismissRequest = { showHelpSupportDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Help & Support", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showHelpSupportDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Call helpline
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001234567"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MenuIconTint),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call 24/7 Helpline (1800-123-4567)")
                    }

                    // Chat Support
                    OutlinedButton(
                        onClick = {
                            showHelpSupportDialog = false
                            onNavigateToSupport()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MenuIconTint)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = MenuIconTint)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Live Support Chat & Tickets", color = MenuIconTint, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Frequently Asked Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)

                    val faqs = listOf(
                        "How do I reschedule a nurse appointment?" to "Open 'Appointments' tab, select your booking, and tap 'Reschedule'.",
                        "What is the cancellation policy?" to "Free cancellation up to 2 hours prior to scheduled visit.",
                        "Are home healthcare nurses certified?" to "Yes, all nurses are certified GNM/B.Sc Registered Nurses."
                    )

                    faqs.forEach { (q, a) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF8FAFC),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(q, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(a, fontSize = 11.5.sp, color = TextMutedSlate)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpSupportDialog = false }) {
                    Text("Close", color = MenuIconTint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ==========================================
    // 6. SETTINGS MODAL
    // ==========================================
    if (showSettingsDialog) {
        var pushNotificationsEnabled by remember { mutableStateOf(true) }
        var smsUpdatesEnabled by remember { mutableStateOf(true) }
        var selectedLang by remember { mutableStateOf("English") }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                    IconButton(onClick = { showSettingsDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedSlate)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Push Notifications", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                            Text("Appointment reminders & alerts", fontSize = 11.5.sp, color = TextMutedSlate)
                        }
                        Switch(
                            checked = pushNotificationsEnabled,
                            onCheckedChange = { pushNotificationsEnabled = it }
                        )
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SMS & WhatsApp Updates", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                            Text("Get OTP & invoice on phone", fontSize = 11.5.sp, color = TextMutedSlate)
                        }
                        Switch(
                            checked = smsUpdatesEnabled,
                            onCheckedChange = { smsUpdatesEnabled = it }
                        )
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("App Language", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDarkNavy)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("English", "हिंदी", "ಕನ್ನಡ").forEach { lang ->
                                FilterChip(
                                    selected = selectedLang == lang,
                                    onClick = { selectedLang = lang },
                                    label = { Text(lang, fontSize = 12.sp) }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Text("App Version 2.4.0 (Build 2026.08)", fontSize = 11.sp, color = TextMutedSlate)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSettingsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MenuIconTint),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ==========================================
// PROFILE MENU ITEM ROW (1:1 Reference)
// ==========================================
@Composable
private fun ProfileMenuItemRow(
    icon: ImageVector,
    title: String,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Icon + Menu Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MenuIconTint,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDarkNavy
            )
        }

        // Right-facing Chevron
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = ChevronTint,
            modifier = Modifier.size(22.dp)
        )
    }
}
