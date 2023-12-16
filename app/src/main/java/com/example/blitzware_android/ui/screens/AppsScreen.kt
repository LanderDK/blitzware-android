package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.blitzware_android.model.Application
import com.example.blitzware_android.ui.viewmodels.ApplicationUiState
import com.example.blitzware_android.ui.viewmodels.ApplicationViewModel

@Composable
fun AppsScreen(
    applicationViewModel: ApplicationViewModel = viewModel(factory = ApplicationViewModel.Factory),
) {
    val applications by applicationViewModel.applications.collectAsState()
    var isDialogVisible by remember { mutableStateOf(false) }
    var newAppName by remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome back, ${applicationViewModel.account?.account?.username}!",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { isDialogVisible = true },
                modifier = Modifier.padding(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(25, 118, 210)
                    )
                }
            )
        }
        Column {
            when (applicationViewModel.applicationUiState) {
                is ApplicationUiState.Loading -> {
                    Text(text = "Loading applications...")
                }
                is ApplicationUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(applications) { application ->
                            ApplicationItem(application = application, applicationViewModel = applicationViewModel)
                        }
                    }
                }
                is ApplicationUiState.Error -> {
                    Text(text = "Error loading applications!")
                }
            }
            if (isDialogVisible) {
                AlertDialog(
                    onDismissRequest = {
                        newAppName = ""
                        isDialogVisible = false },
                    title = { Text("Create an application") },
                    text = {
                        TextField(
                            value = newAppName,
                            onValueChange = { newAppName = it },
                            label = { Text("Application name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newAppName.isNotEmpty() && newAppName.isNotBlank()) {
                                    isDialogVisible = false
                                    applicationViewModel.createApplication(newAppName)
                                    newAppName = ""
                                }
                            }
                        ) {
                            Text("Create")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                isDialogVisible = false
                                newAppName = ""
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ApplicationItem(application: Application, applicationViewModel: ApplicationViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                expanded = true
            }
        ,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = application.name,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (application.status == 1) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green
                )
                Text(
                    text = "Enabled",
                    color = Color.Green,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = null,
                    tint = Color.Red
                )
                Text(
                    text = "Disabled",
                    color = Color.Red,
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
            }) {
                Text("App-Panel")
//                Text("App-Panel", modifier = Modifier.clickable {
//                    expanded = false
//                })
            }
            if (application.status == 1) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    applicationViewModel.updateApplicationById(application.copy(status = 0))
                }) {
                    Text("Disable")
                }
            } else {
                DropdownMenuItem(onClick = {
                    expanded = false
                    applicationViewModel.updateApplicationById(application.copy(status = 1))
                }) {
                    Text("Enable")
                }
            }

            DropdownMenuItem(onClick = {
                expanded = false
                applicationViewModel.deleteApplicationById(application)
            }) {
                Text("Delete")
            }
        }
    }
}
