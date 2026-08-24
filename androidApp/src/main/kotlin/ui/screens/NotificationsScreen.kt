package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.HealthcareNotification
import com.example.data.models.NotificationType
import com.example.ui.viewmodel.MainViewModel

// Custom Color Palette strictly matching Reference (Notifications.png)
private val ScreenBackground = Color(0xFFF9FAFB)     // Very light gray background
private val DarkTitleText = Color(0xFF0F172A)        // Deep dark slate/navy for header & titles
private val MutedBodyText = Color(0xFF64748B)        // Slate gray for messages & timestamps
private val ContainerBorder = Color(0xFFF1F5F9)      // Subtle outer container border
private val DividerColor = Color(0xFFF1F5F9)         // Subtle notification row divider
private val PrimaryPurple = Color(0xFF6D28D9)        // Brand purple for icons & action button

// Icon Container Pastels (Reference 1:1)
private val PurpleIconBg = Color(0xFFEDE9FE)         // Light pastel purple for Calendar & Nurse
private val PurpleIconTint = Color(0xFF6D28D9)       // Vibrant purple icon tint
private val GreenIconBg = Color(0xFFDCFCE7)          // Light pastel mint green for Payment
private val GreenIconTint = Color(0xFF16A34A)        // Vibrant green icon tint
private val OrangeIconBg = Color(0xFFFFEDD5)         // Light pastel peach/orange for Special Offer
private val OrangeIconTint = Color(0xFFEA580C)       // Vibrant orange icon tint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onAppointmentClick: (String) -> Unit = {},
    onPaymentSuccessClick: (String) -> Unit = {},
    onNurseClick: (String) -> Unit = {},
    onServiceClick: (String) -> Unit = {}
) {
    val notifications by viewModel.notifications.collectAsState()
    var isExpandedView by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground),
        containerColor = ScreenBackground,
        topBar = {
            NotificationsTopHeader(
                onBackClick = onBackClick,
                hasNotifications = notifications.isNotEmpty(),
                onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
                onClearAll = { viewModel.clearAllNotifications() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (notifications.isEmpty()) {
                // Empty State
                NotificationsEmptyState()
            } else {
                // Main Notification Container Card (1:1 with Reference)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, ContainerBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color(0x08000000),
                            spotColor = Color(0x0A000000)
                        )
                        .testTag("notification_container_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        // Display notifications (all or primary 4 items)
                        val displayList = if (isExpandedView) notifications else notifications.take(4)

                        displayList.forEachIndexed { index, notification ->
                            NotificationRowItem(
                                notification = notification,
                                onClick = {
                                    viewModel.markNotificationAsRead(notification.id)
                                    when (notification.type) {
                                        NotificationType.APPOINTMENT_REMINDER -> {
                                            val apptId = notification.relatedAppointmentId ?: "BMJ1254789"
                                            onAppointmentClick(apptId)
                                        }
                                        NotificationType.PAYMENT_SUCCESS -> {
                                            val apptId = notification.relatedAppointmentId ?: "BMJ1254789"
                                            onPaymentSuccessClick(apptId)
                                        }
                                        NotificationType.NURSE_ASSIGNED -> {
                                            val nurseId = notification.relatedNurseId ?: "NURSE_101"
                                            onNurseClick(nurseId)
                                        }
                                        NotificationType.SPECIAL_OFFER -> {
                                            val serviceId = notification.relatedServiceId ?: "LAB_1"
                                            onServiceClick(serviceId)
                                        }
                                    }
                                }
                            )

                            // Horizontal Divider between items
                            if (index < displayList.size - 1 || (!isExpandedView && notifications.size > 4) || true) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = DividerColor,
                                    thickness = 1.dp
                                )
                            }
                        }

                        // Bottom Action: View All Notifications / Show Less
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isExpandedView = !isExpandedView
                                }
                                .padding(vertical = 18.dp)
                                .testTag("view_all_notifications_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isExpandedView) "Show Less" else "View All Notifications",
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// TOP HEADER COMPONENT (1:1 Reference)
// ==========================================
@Composable
private fun NotificationsTopHeader(
    onBackClick: () -> Unit,
    hasNotifications: Boolean,
    onMarkAllAsRead: () -> Unit,
    onClearAll: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        color = ScreenBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Back Arrow Button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTitleText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Title: Notifications
                Text(
                    text = "Notifications",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTitleText,
                    modifier = Modifier.weight(1f)
                )

                if (hasNotifications) {
                    // Action Menu
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = MutedBodyText,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mark all as read", fontSize = 13.5.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = null,
                                        tint = PrimaryPurple,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    onMarkAllAsRead()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Clear all", fontSize = 13.5.sp, color = Color(0xFFDC2626)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    onClearAll()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Subtle divider below header
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp
            )
        }
    }
}

// ==========================================
// NOTIFICATION ROW ITEM (1:1 Reference)
// ==========================================
@Composable
private fun NotificationRowItem(
    notification: HealthcareNotification,
    onClick: () -> Unit
) {
    // Determine icon, colors based on NotificationType
    val (iconVector, iconBgColor, iconTintColor) = when (notification.type) {
        NotificationType.APPOINTMENT_REMINDER -> Triple(
            Icons.Outlined.EventAvailable,
            PurpleIconBg,
            PurpleIconTint
        )
        NotificationType.PAYMENT_SUCCESS -> Triple(
            Icons.Default.CheckCircle,
            GreenIconBg,
            GreenIconTint
        )
        NotificationType.NURSE_ASSIGNED -> Triple(
            Icons.Outlined.PersonPin,
            PurpleIconBg,
            PurpleIconTint
        )
        NotificationType.SPECIAL_OFFER -> Triple(
            Icons.Outlined.CrisisAlert,
            OrangeIconBg,
            OrangeIconTint
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (!notification.isRead) Color(0xFFFAF8FF) else Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("notification_item_${notification.id}"),
        verticalAlignment = Alignment.Top
    ) {
        // Left Icon Container (Rounded square 48x48)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = iconBgColor,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = notification.title,
                    tint = iconTintColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center & Right Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 1.dp)
        ) {
            // Row with Title and Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Title
                Text(
                    text = notification.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTitleText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Timestamp (Right Aligned)
                Text(
                    text = notification.timestamp,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = MutedBodyText
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Message text
            Text(
                text = notification.message,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontWeight = if (!notification.isRead) FontWeight.Medium else FontWeight.Normal,
                color = if (!notification.isRead) Color(0xFF334155) else MutedBodyText
            )
        }
    }
}

// ==========================================
// EMPTY STATE COMPONENT
// ==========================================
@Composable
private fun NotificationsEmptyState() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, ContainerBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = PurpleIconBg,
                modifier = Modifier.size(72.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "No Notifications",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTitleText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "You're all caught up!",
                fontSize = 14.sp,
                color = MutedBodyText,
                textAlign = TextAlign.Center
            )
        }
    }
}
