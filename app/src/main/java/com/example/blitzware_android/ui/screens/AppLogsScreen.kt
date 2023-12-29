package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.blitzware_android.model.AppLog
import com.example.blitzware_android.ui.viewmodels.AppLogUiState
import com.example.blitzware_android.ui.viewmodels.AppLogViewModel
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * App logs screen
 *
 * @param appLogViewModel
 */
@Composable
fun AppLogsScreen(appLogViewModel: AppLogViewModel = viewModel(factory = AppLogViewModel.Factory)) {
    val appLogs by appLogViewModel.appLogs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 75.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Application Logs - ${appLogViewModel.application.value?.name ?: "N/A"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        when (appLogViewModel.appLogUiState) {
            is AppLogUiState.Loading -> {
                Text(text = "Loading application logs...")
            }
            is AppLogUiState.Success -> {
                LazyColumn {
                    items(appLogs) { appLog ->
                        AppLogRow(appLog = appLog, appLogViewModel = appLogViewModel)
                    }
                }
            }
            is AppLogUiState.Error -> {
                Text(text = (appLogViewModel.appLogUiState as AppLogUiState.Error).message)
            }
        }
    }
}

/**
 * App log row
 *
 * @param appLog
 * @param appLogViewModel
 */
@Composable
fun AppLogRow(appLog: AppLog, appLogViewModel: AppLogViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(appLog.date)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .clickable { expanded = true }
    ) {
        Text(appLog.username, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        AppLogDetailRow("Date:", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date!!))
        AppLogDetailRow("Action:", appLog.action)
        AppLogDetailRow("IP:", appLog.ip)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                appLogViewModel.deleteAppLogById(appLog)
            }) {
                Text("Delete")
            }
        }
    }
}

/**
 * App log detail row
 *
 * @param title
 * @param value
 */
@Composable
fun AppLogDetailRow(title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "$title ",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontSize = 14.sp
        )
    }
}