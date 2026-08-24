package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PurplePrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

// Brand Color Palette matching Reference
private val ScreenBackground = Color(0xFFFCF8FD)
private val BrandPurple = Color(0xFF6E4D90)          // Primary active purple from reference
private val BrandPurpleDark = Color(0xFF1E153A)      // Deep dark navy/purple heading text
private val BrandPurpleLight = Color(0xFFF3E8FF)     // Soft lavender circle background
private val BrandPurpleRing = Color(0xFF9F83BF)      // Ring highlight around active day/step
private val TextMutedGray = Color(0xFF94A3B8)        // Weekday header & subtitle text
private val DateDisabledGray = Color(0xFFCBD5E1)     // Previous month disabled date color
private val BorderSubtle = Color(0xFFF3EDF8)         // Card outline border

data class TimeSlotItem(
    val time: String,
    val isAvailable: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    serviceId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onProceedToAddress: (String) -> Unit
) {
    val service = remember(serviceId) {
        viewModel.services.firstOrNull { it.id.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.firstOrNull { it.category.name.equals(serviceId, ignoreCase = true) }
            ?: viewModel.services.first()
    }

    // Step state in booking flow: 1: Service, 2: Date & Time, 3: Address, 4: Confirm
    var currentStep by remember { mutableIntStateOf(2) }

    // Calendar State (Defaults to May 2025 as in reference image, or can dynamically navigate)
    val calendar = remember {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DAY_OF_MONTH, 24)
        }
    }

    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(24) }

    // Time Slot Selection (Default to 10:00 AM matching reference)
    var selectedTimeSlot by remember { mutableStateOf("10:00 AM") }

    // Sample 9 Time Slots matching Reference
    val timeSlots = remember {
        listOf(
            TimeSlotItem("09:00 AM", isAvailable = true),
            TimeSlotItem("09:30 AM", isAvailable = true),
            TimeSlotItem("10:00 AM", isAvailable = true),
            TimeSlotItem("10:30 AM", isAvailable = true),
            TimeSlotItem("11:00 AM", isAvailable = false), // Disabled/Unavailable in reference
            TimeSlotItem("11:30 AM", isAvailable = true),
            TimeSlotItem("02:00 PM", isAvailable = true),
            TimeSlotItem("02:30 PM", isAvailable = true),
            TimeSlotItem("03:00 PM", isAvailable = true)
        )
    }

    // Dynamic month display name (e.g. "May 2025")
    val monthYearTitle = remember(selectedYear, selectedMonth) {
        val tempCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(tempCal.time)
    }

    // Calculate calendar grid for the selected month
    val calendarDays = remember(selectedYear, selectedMonth) {
        generateCalendarGrid(selectedYear, selectedMonth)
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            // ==========================================
            // HEADER (Back Arrow + Book Appointment Title)
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
                        .testTag("booking_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrandPurpleDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Book Appointment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        bottomBar = {
            // ==========================================
            // FIXED BOTTOM CTA (Next →)
            // ==========================================
            Surface(
                color = ScreenBackground,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = {
                            val formattedDate = "$selectedDay $monthYearTitle"
                            viewModel.bookingDate.value = formattedDate
                            viewModel.bookingTimeSlot.value = selectedTimeSlot
                            onProceedToAddress(service.id)
                        },
                        enabled = selectedDay > 0 && selectedTimeSlot.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandPurple,
                            disabledContainerColor = BrandPurple.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = BrandPurple)
                            .testTag("booking_next_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Next",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
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
            // 4-STEP BOOKING PROGRESS INDICATOR
            // ==========================================
            BookingStepProgressIndicator(
                currentStep = currentStep,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // CALENDAR SECTION (Large Rounded Card)
            // ==========================================
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_calendar_card")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp)
                ) {
                    // Month Navigation Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (selectedMonth == 0) {
                                    selectedMonth = 11
                                    selectedYear -= 1
                                } else {
                                    selectedMonth -= 1
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Month",
                                tint = BrandPurpleDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Text(
                            text = monthYearTitle,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurpleDark
                        )

                        IconButton(
                            onClick = {
                                if (selectedMonth == 11) {
                                    selectedMonth = 0
                                    selectedYear += 1
                                } else {
                                    selectedMonth += 1
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Month",
                                tint = BrandPurpleDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Weekdays Header (SUN MON TUE WED THU FRI SAT)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val weekdays = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
                        weekdays.forEach { day ->
                            Text(
                                text = day,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMutedGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Calendar Days Grid (7 columns)
                    val rows = calendarDays.chunked(7)
                    rows.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            week.forEach { dayItem ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (dayItem.isCurrentMonth) {
                                        val isSelected = (dayItem.day == selectedDay)
                                        if (isSelected) {
                                            // Selected Date (Purple circle with outer ring matching reference)
                                            Box(
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .border(2.dp, BrandPurpleRing, CircleShape)
                                                    .padding(2.dp)
                                                    .clip(CircleShape)
                                                    .background(BrandPurple)
                                                    .clickable { selectedDay = dayItem.day },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${dayItem.day}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        } else {
                                            // Available Date
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .clickable { selectedDay = dayItem.day },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${dayItem.day}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = BrandPurpleDark
                                                )
                                            }
                                        }
                                    } else {
                                        // Previous/Next month muted gray date
                                        Text(
                                            text = "${dayItem.day}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = DateDisabledGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SELECT TIME SECTION
            // ==========================================
            Text(
                text = "Select Time",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPurpleDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3-Column Grid of Time Slots
            val timeSlotRows = timeSlots.chunked(3)
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                timeSlotRows.forEach { rowSlots ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowSlots.forEach { slot ->
                            TimeSlotButton(
                                slot = slot,
                                isSelected = (selectedTimeSlot == slot.time),
                                onClick = {
                                    if (slot.isAvailable) {
                                        selectedTimeSlot = slot.time
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill remaining spaces if row has fewer than 3 items
                        if (rowSlots.size < 3) {
                            repeat(3 - rowSlots.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// ==========================================
// 4-STEP PROGRESS INDICATOR COMPOSABLE
// ==========================================
@Composable
fun BookingStepProgressIndicator(
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Step 1: Service (Completed)
        StepItem(
            stepNumber = 1,
            label = "Service",
            isCompleted = true,
            isActive = currentStep == 1,
            modifier = Modifier.weight(1f)
        )

        // Step 2: Date & Time (Active)
        StepItem(
            stepNumber = 2,
            label = "Date & Time",
            isCompleted = false,
            isActive = currentStep == 2,
            modifier = Modifier.weight(1.2f)
        )

        // Step 3: Address (Upcoming)
        StepItem(
            stepNumber = 3,
            label = "Address",
            isCompleted = false,
            isActive = currentStep == 3,
            modifier = Modifier.weight(1f)
        )

        // Step 4: Confirm (Upcoming)
        StepItem(
            stepNumber = 4,
            label = "Confirm",
            isCompleted = false,
            isActive = currentStep == 4,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StepItem(
    stepNumber: Int,
    label: String,
    isCompleted: Boolean,
    isActive: Boolean,
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
            // Active: Darker purple circle with outer light ring & number 2
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(2.dp, BrandPurpleRing.copy(alpha = 0.6f), CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(BrandPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$stepNumber",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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
            fontSize = 11.5.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) BrandPurpleDark else TextMutedGray,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ==========================================
// TIME SLOT BUTTON COMPOSABLE
// ==========================================
@Composable
fun TimeSlotButton(
    slot: TimeSlotItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (slot.isAvailable) {
        if (isSelected) {
            // Selected State: Purple background with light purple outline & shadow
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BrandPurple,
                border = BorderStroke(2.dp, BrandPurpleRing),
                shadowElevation = 2.dp,
                modifier = modifier
                    .height(46.dp)
                    .clickable { onClick() }
                    .testTag("time_slot_${slot.time}_selected")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot.time,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        } else {
            // Available State: White background with subtle border
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF1E5E7)),
                shadowElevation = 0.5.dp,
                modifier = modifier
                    .height(46.dp)
                    .clickable { onClick() }
                    .testTag("time_slot_${slot.time}")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot.time,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandPurpleDark
                    )
                }
            }
        }
    } else {
        // Unavailable State: Very light background with disabled muted text
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFBF8FB),
            border = BorderStroke(1.dp, Color(0xFFF1F0F5)),
            modifier = modifier.height(46.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slot.time,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = DateDisabledGray
                )
            }
        }
    }
}

// ==========================================
// CALENDAR GRID GENERATOR
// ==========================================
data class CalendarDay(
    val day: Int,
    val isCurrentMonth: Boolean
)

fun generateCalendarGrid(year: Int, month: Int): List<CalendarDay> {
    val result = mutableListOf<CalendarDay>()

    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    // Day of week for 1st of this month (1 = Sunday, 2 = Monday ... 7 = Saturday)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    val maxDaysThisMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Previous month info
    val prevCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        add(Calendar.MONTH, -1)
    }
    val maxDaysPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Leading days from previous month
    val leadingDaysCount = firstDayOfWeek - Calendar.SUNDAY
    for (i in (leadingDaysCount - 1) downTo 0) {
        result.add(CalendarDay(day = maxDaysPrevMonth - i, isCurrentMonth = false))
    }

    // Days of current month
    for (i in 1..maxDaysThisMonth) {
        result.add(CalendarDay(day = i, isCurrentMonth = true))
    }

    // Trailing days to fill the final week
    var nextMonthDay = 1
    while (result.size % 7 != 0) {
        result.add(CalendarDay(day = nextMonthDay++, isCurrentMonth = false))
    }

    return result
}
