package com.example.blitzware_android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.blitzware_android.navigation.Screens
import com.example.blitzware_android.ui.viewmodels.ApplicationUiState
import com.example.blitzware_android.ui.viewmodels.ApplicationViewModel

/**
 * App detail screen
 *
 * @param navController
 * @param applicationViewModel
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppDetailScreen(
    navController: NavController,
    applicationViewModel: ApplicationViewModel = viewModel(factory = ApplicationViewModel.Factory)
) {
    var application by remember { mutableStateOf(applicationViewModel.application) }

    val scrollState = rememberScrollState()

    LaunchedEffect(applicationViewModel) {
        applicationViewModel.getSelectedApplication()
        application = applicationViewModel.application
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 75.dp)
            .verticalScroll(state = scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    applicationViewModel.deleteSelectedApplicationEntry()
                    navController.navigate(Screens.AppsScreen.name)
                          },
                modifier = Modifier.padding(end = 8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color(25, 118, 210)
                    )
                }
            )
            Text(
                text = "Application Panel - ${application.value?.name ?: "N/A"}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
        ) {
            when (applicationViewModel.applicationUiState) {
                is ApplicationUiState.Loading -> {
                    Text(text = "Loading application...")
                }
                is ApplicationUiState.Success -> {
                    ApplicationDetailItem("Name:", application.value?.name ?: "N/A")
                    ApplicationDetailItem("Secret:", application.value?.secret ?: "N/A")
                    ApplicationDetailItem("Version:", application.value?.version ?: "N/A")
                    ApplicationDetailItem(
                        "Status:",
                        if (application.value?.status == 1) "Enabled" else "Disabled",
                        color = if (application.value?.status == 1) Color.Green else Color.Red
                    )
                    ApplicationDetailItem(
                        "Developer Mode:",
                        if (application.value?.developerMode == 1) "Enabled" else "Disabled",
                        color = if (application.value?.developerMode == 1) Color.Green else Color.Red
                    )
                    ApplicationDetailItem(
                        "2FA:",
                        if (application.value?.twoFactorAuth == 1) "Enabled" else "Disabled",
                        color = if (application.value?.twoFactorAuth == 1) Color.Green else Color.Red
                    )
                    ApplicationDetailItem(
                        "HWID Lock:",
                        if (application.value?.hwidCheck == 1) "Enabled" else "Disabled",
                        color = if (application.value?.hwidCheck == 1) Color.Green else Color.Red
                    )
                    ApplicationDetailItem(
                        "Free Mode:",
                        if (application.value?.freeMode == 1) "Enabled" else "Disabled",
                        color = if (application.value?.freeMode == 1) Color.Green else Color.Red
                    )
                    ApplicationDetailItem(
                        "Integrity Check:",
                        if (application.value?.integrityCheck == 1) "Enabled" else "Disabled",
                        color = if (application.value?.integrityCheck == 1) Color.Green else Color.Red
                    )
                }
                is ApplicationUiState.Error -> {
                    Text(text = (applicationViewModel.applicationUiState as ApplicationUiState.Error).message)
                }
            }
        }
    }
}

/**
 * Application detail item
 *
 * @param title
 * @param value
 * @param color
 */
@Composable
fun ApplicationDetailItem(title: String, value: String, color: Color? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = value,
            color = color ?: Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}