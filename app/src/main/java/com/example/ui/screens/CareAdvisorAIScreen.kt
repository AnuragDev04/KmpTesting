package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareAdvisorAIScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onBookServiceClick: (String) -> Unit
) {
    val query by viewModel.aiQuery.collectAsState()
    val patientAge by viewModel.aiPatientAge.collectAsState()
    val condition by viewModel.aiCondition.collectAsState()
    val responseText by viewModel.aiResponse.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Healthcare Advisor", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TealPrimaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CoralHighlight,
                        modifier = Modifier.size(36.dp)
                    )
                    Column {
                        Text(
                            text = "Smart Care Package Recommender",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimaryDark
                        )
                        Text(
                            text = "Describe patient symptoms or recovery needs to get instant AI-guided nursing package recommendations.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Input Form Card
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
                        text = "Patient Symptoms & Condition",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )

                    OutlinedTextField(
                        value = patientAge,
                        onValueChange = { viewModel.aiPatientAge.value = it },
                        label = { Text("Patient Age") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = condition,
                        onValueChange = { viewModel.aiCondition.value = it },
                        label = { Text("Medical History / Current Illness") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.aiQuery.value = it },
                        label = { Text("What care or assistance is needed?") },
                        placeholder = { Text("e.g. Father had knee surgery, needs dressing and help walking.") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.askAiCareAdvisor() },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing Patient Needs...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Get AI Recommendation", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // AI Response Output Card
            if (responseText != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = TealPrimary)
                            Text("AI Advisory Analysis", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                        }

                        HorizontalDivider()

                        Text(
                            text = responseText!!,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Button(
                            onClick = { onBookServiceClick("NURSING_1") },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Proceed to Book Care Package", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
