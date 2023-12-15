package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blitzware_android.model.Log
import com.example.blitzware_android.ui.viewmodels.AccountViewModel
import com.example.blitzware_android.ui.viewmodels.LogUiState
import com.example.blitzware_android.ui.viewmodels.LogViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AccountLogsScreen(
    accountViewModel: AccountViewModel,
    logViewModel: LogViewModel,
    navController: NavHostController
) {
    val logs by logViewModel.logs.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color(25, 118, 210)
                    )
                }
            )
            Text(
                text = "Account Logs",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        when (logViewModel.logUiState) {
            is LogUiState.Loading -> {
                Text(text = "Loading messages...")
            }
            is LogUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                ) {
                    items(logs) { log ->
                        Log(log = log, onDelete = {
                            logViewModel.deleteLogById(log)
                        })
                    }
                }
            }
            is LogUiState.Error -> {
                Text(text = "Error loading messages!")
            }
        }
    }
}

@Composable
fun Log(log: Log, onDelete: () -> Unit) {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(log.date)

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = "${log.action}: ${log.message}"
            )
            Text(
                text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date),
                color = Color.Gray
            )
        }
        IconButton(
            onClick = { onDelete() },
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }
}
