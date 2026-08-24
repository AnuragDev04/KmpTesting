package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MainViewModel
import kotlin.math.roundToInt

// Color Palette matching Reference
private val ScreenBackground = Color(0xFFFCF8FD)
private val BrandPurple = Color(0xFF6E4D90)          // Primary purple
private val BrandPurpleDark = Color(0xFF1E153A)      // Deep dark navy/purple heading text
private val BrandPurpleLight = Color(0xFFF3E8FF)     // Soft lavender circle background
private val StepOrange = Color(0xFFF26822)           // Vibrant active step & Next button orange
private val StepOrangeLight = Color(0xFFFFEDD5)      // Light orange tint
private val TextMutedGray = Color(0xFF94A3B8)        // Subtitle / connecting line gray
private val BorderSubtle = Color(0xFFF1EEF6)         // Card border

data class SavedAddressItem(
    val id: String,
    val title: String,
    val address: String,
    val iconType: AddressIconType
)

enum class AddressIconType {
    HOME,
    OFFICE,
    OTHER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAddressScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onProceedToConfirm: (String) -> Unit
) {
    val context = LocalContext.current

    val service = remember(serviceId) {
        viewModel.services.firstOrNull { it.id.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.firstOrNull { it.category.name.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.first()
    }

    // Saved Addresses list with initial items from reference image
    var savedAddresses by remember {
        mutableStateOf(
            listOf(
                SavedAddressItem(
                    id = "addr_home",
                    title = "Home",
                    address = "12, Park Street, Bangalore,\nKarnataka - 560001",
                    iconType = AddressIconType.HOME
                ),
                SavedAddressItem(
                    id = "addr_office",
                    title = "Office",
                    address = "3rd Floor, Block A, Koramangala,\nBangalore - 560095",
                    iconType = AddressIconType.OFFICE
                ),
                SavedAddressItem(
                    id = "addr_other",
                    title = "Other",
                    address = "78, MG Road, Bangalore - 560001",
                    iconType = AddressIconType.OTHER
                )
            )
        )
    }

    // Selected Address ID (Defaults to Home / Current location matching reference)
    var selectedAddressId by remember { mutableStateOf("addr_home") }

    // Map Pan Offset State for interactive map dragging
    var mapOffset by remember { mutableStateOf(Offset.Zero) }

    // Add New Address Modal / BottomSheet State
    var showAddAddressSheet by remember { mutableStateOf(false) }

    // Resolve Map Background Asset
    val mapResId = remember {
        val customMap = context.resources.getIdentifier("healthcare_map_bg_1787042512644", "drawable", context.packageName)
        if (customMap != 0) customMap else {
            context.resources.getIdentifier("img_hero_banner_1786014371290", "drawable", context.packageName)
        }
    }

    // Bottom Sheet for adding new address
    if (showAddAddressSheet) {
        AddNewAddressBottomSheet(
            onDismiss = { showAddAddressSheet = false },
            onAddressAdded = { newTitle, newAddr, newType ->
                val newId = "addr_${System.currentTimeMillis()}"
                val newItem = SavedAddressItem(
                    id = newId,
                    title = newTitle,
                    address = newAddr,
                    iconType = newType
                )
                savedAddresses = savedAddresses + newItem
                selectedAddressId = newId
                showAddAddressSheet = false
            }
        )
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            // ==========================================
            // HEADER (Back Arrow + Select Address Title)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("select_address_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPurpleDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Select Address",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM CTA (Orange Next Button)
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
                    val selectedItem = savedAddresses.firstOrNull { it.id == selectedAddressId }
                    val isEnabled = selectedItem != null

                    Button(
                        onClick = {
                            selectedItem?.let {
                                viewModel.bookingAddress.value = it.address.replace("\n", " ")
                                onProceedToConfirm(service.id)
                            }
                        },
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StepOrange,
                            disabledContainerColor = StepOrange.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = StepOrange)
                            .testTag("select_address_next_button")
                    ) {
                        Text(
                            text = "Next",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
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
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // ==========================================
            // 4-STEP PROGRESS INDICATOR (Step 3 Orange Active)
            // ==========================================
            AddressStepProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // MAP SECTION WITH CENTRAL PIN & FLOATING CURRENT LOCATION CARD
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
            ) {
                // Interactive Realistic Map View
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFFE2F1E8))
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                mapOffset = Offset(
                                    x = (mapOffset.x + dragAmount.x).coerceIn(-120f, 120f),
                                    y = (mapOffset.y + dragAmount.y).coerceIn(-60f, 60f)
                                )
                            }
                        }
                ) {
                    // Map Background Image
                    if (mapResId != 0) {
                        Image(
                            painter = painterResource(id = mapResId),
                            contentDescription = "Map view",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .offset { IntOffset(mapOffset.x.roundToInt(), mapOffset.y.roundToInt()) }
                        )
                    }

                    // Map Street Names & Vector Annotations matching reference
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset { IntOffset(mapOffset.x.roundToInt(), mapOffset.y.roundToInt()) }
                    ) {
                        // "Hospital / 145 Elm St"
                        Text(
                            text = "Hospital\n145 Elm St",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 65.dp, top = 8.dp)
                        )

                        // "Elm St" label
                        Text(
                            text = "Elm St",
                            fontSize = 9.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 40.dp, top = 70.dp)
                        )

                        // "Oak Ave" vertical text
                        Text(
                            text = "Oak Ave",
                            fontSize = 9.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 20.dp, end = 80.dp)
                        )

                        // "Central Park" green zone annotation
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 35.dp, end = 25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Central\nPark",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF335C49),
                                textAlign = TextAlign.Center
                            )
                        }

                        // "You are here" Blue Dot + City Library
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 30.dp, bottom = 45.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF3B82F6),
                                border = BorderStroke(2.dp, Color.White),
                                shadowElevation = 2.dp,
                                modifier = Modifier.size(14.dp)
                            ) {}
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "You are here",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "City Library",
                                fontSize = 9.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        // Selected Address Pin Center ("11. Select Address" & "11 Maple Blvd")
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "11. Select Address",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E153A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            // Purple Location Pin
                            Surface(
                                shape = CircleShape,
                                color = BrandPurple,
                                shadowElevation = 4.dp,
                                border = BorderStroke(2.dp, Color.White),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Selected Location",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "11 Maple Blvd",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                // ==========================================
                // FLOATING CURRENT LOCATION CARD
                // ==========================================
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .testTag("select_address_current_location_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Target / MyLocation Icon
                        Surface(
                            shape = CircleShape,
                            color = BrandPurpleLight,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.MyLocation,
                                    contentDescription = "Location",
                                    tint = BrandPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Current Location Address Text
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Current Location",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandPurpleDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "12, Park Street, Bangalore,\nKarnataka - 560001",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                lineHeight = 16.sp
                            )
                        }

                        // Right Target / GPS Locate Button
                        IconButton(
                            onClick = {
                                selectedAddressId = "addr_home"
                                mapOffset = Offset.Zero
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MyLocation,
                                contentDescription = "Center Current Location",
                                tint = BrandPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // SAVED ADDRESSES SECTION
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Saved Addresses",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Saved Address Cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    savedAddresses.forEach { item ->
                        val isSelected = (item.id == selectedAddressId)
                        SavedAddressCard(
                            item = item,
                            isSelected = isSelected,
                            onClick = { selectedAddressId = item.id }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ==========================================
                // ＋ ADD NEW ADDRESS BUTTON
                // ==========================================
                Button(
                    onClick = { showAddAddressSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = BrandPurple)
                        .testTag("add_new_address_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Address",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ==========================================
// SAVED ADDRESS CARD COMPOSABLE
// ==========================================
@Composable
fun SavedAddressCard(
    item: SavedAddressItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFAF5FF) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) BrandPurple else BorderSubtle
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("address_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Purple Circular Icon Container
            Surface(
                shape = CircleShape,
                color = BrandPurpleLight,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val icon: ImageVector = when (item.iconType) {
                        AddressIconType.HOME -> Icons.Outlined.Home
                        AddressIconType.OFFICE -> Icons.Outlined.WorkOutline
                        AddressIconType.OTHER -> Icons.Outlined.LocationOn
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = item.title,
                        tint = BrandPurple,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Address Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.address,
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 16.sp
                )
            }

            // Right Chevron Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isSelected) BrandPurple else Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ==========================================
// 4-STEP ADDRESS PROGRESS INDICATOR (Step 3 Orange Active)
// ==========================================
@Composable
fun AddressStepProgressIndicator(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Step 1: Service (Completed Purple Check)
        ConnectedStepItem(
            stepNumber = 1,
            label = "Service",
            isCompleted = true,
            isActive = false,
            activeColor = BrandPurple,
            modifier = Modifier.weight(1f)
        )

        // Connecting Line 1-2
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(1.5.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(Color(0xFFE2E8F0))
        )

        // Step 2: Date & Time (Completed Purple Check)
        ConnectedStepItem(
            stepNumber = 2,
            label = "Date & Time",
            isCompleted = true,
            isActive = false,
            activeColor = BrandPurple,
            modifier = Modifier.weight(1.2f)
        )

        // Connecting Line 2-3
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(1.5.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(Color(0xFFE2E8F0))
        )

        // Step 3: Address (Active Orange 3)
        ConnectedStepItem(
            stepNumber = 3,
            label = "Address",
            isCompleted = false,
            isActive = true,
            activeColor = StepOrange,
            modifier = Modifier.weight(1f)
        )

        // Connecting Line 3-4
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(1.5.dp)
                .align(Alignment.CenterVertically)
                .offset(y = (-10).dp)
                .background(Color(0xFFE2E8F0))
        )

        // Step 4: Confirm (Upcoming Lavender 4)
        ConnectedStepItem(
            stepNumber = 4,
            label = "Confirm",
            isCompleted = false,
            isActive = false,
            activeColor = BrandPurple,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ConnectedStepItem(
    stepNumber: Int,
    label: String,
    isCompleted: Boolean,
    isActive: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCompleted) {
            // Completed: Purple circle with white checkmark
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
        } else if (isActive) {
            // Active Step 3: Orange filled circular badge
            Surface(
                shape = CircleShape,
                color = StepOrange,
                shadowElevation = 2.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$stepNumber",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else {
            // Upcoming: Light purple circle with number
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
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) StepOrange else TextMutedGray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ==========================================
// ADD NEW ADDRESS BOTTOM SHEET
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAddressBottomSheet(
    onDismiss: () -> Unit,
    onAddressAdded: (String, String, AddressIconType) -> Unit
) {
    var title by remember { mutableStateOf("Home") }
    var flatHouse by remember { mutableStateOf("") }
    var areaStreet by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Bangalore") }
    var pincode by remember { mutableStateOf("560001") }
    var selectedType by remember { mutableStateOf(AddressIconType.HOME) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Add New Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            // Address Type selector (Home, Office, Other)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    Triple(AddressIconType.HOME, "Home", Icons.Outlined.Home),
                    Triple(AddressIconType.OFFICE, "Office", Icons.Outlined.WorkOutline),
                    Triple(AddressIconType.OTHER, "Other", Icons.Outlined.LocationOn)
                ).forEach { (type, label, icon) ->
                    val isTypeSelected = (selectedType == type)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isTypeSelected) BrandPurpleLight else Color(0xFFF8FAFC),
                        border = BorderStroke(
                            1.dp,
                            if (isTypeSelected) BrandPurple else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clickable {
                                selectedType = type
                                title = label
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isTypeSelected) BrandPurple else Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isTypeSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isTypeSelected) BrandPurple else Color(0xFF475569)
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = flatHouse,
                onValueChange = { flatHouse = it },
                label = { Text("Flat / House No. / Building") },
                placeholder = { Text("e.g. Flat 402, Sunshine Apts") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = areaStreet,
                onValueChange = { areaStreet = it },
                label = { Text("Area / Street / Landmark") },
                placeholder = { Text("e.g. 14th Cross, Indiranagar") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    label = { Text("Pincode") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    val fullAddress = if (flatHouse.isNotBlank() && areaStreet.isNotBlank()) {
                        "$flatHouse, $areaStreet,\n$city - $pincode"
                    } else if (areaStreet.isNotBlank()) {
                        "$areaStreet,\n$city - $pincode"
                    } else {
                        "12, Park Street, Bangalore,\nKarnataka - 560001"
                    }
                    onAddressAdded(title, fullAddress, selectedType)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Save Address",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
