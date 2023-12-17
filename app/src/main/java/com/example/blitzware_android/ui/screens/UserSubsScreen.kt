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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.ui.viewmodels.UserSubUiState
import com.example.blitzware_android.ui.viewmodels.UserSubViewModel

@Composable
fun UserSubsScreen(userSubViewModel: UserSubViewModel = viewModel(factory = UserSubViewModel.Factory)) {
    val userSubs by userSubViewModel.userSubs.collectAsState()
    var isAddDialogVisible by remember { mutableStateOf(false) }
    var isEditDialogVisible by remember { mutableStateOf(false) }

    var newUserSubName by remember { mutableStateOf("") }
    var newUserSubLevel by remember { mutableStateOf("") }

    var updateUserSubId by remember { mutableStateOf("") }
    var updateUserSubName by remember { mutableStateOf("") }
    var updateUserSubLevel by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Subscription Manager - ${userSubViewModel.application.value?.name ?: "N/A"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            //Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    isEditDialogVisible = false
                    isAddDialogVisible = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(25, 118, 210)
                )
            }
        }

        when (userSubViewModel.userSubUiState) {
            is UserSubUiState.Loading -> {
                Text(text = "Loading subscriptions...")
            }
            is UserSubUiState.Success -> {
                LazyColumn {
                    items(userSubs) { userSub ->
                        UserSubRow(userSub = userSub, userSubViewModel = userSubViewModel) {
                            isAddDialogVisible = false
                            updateUserSubId = userSub.id.toString()
                            updateUserSubName = userSub.name
                            updateUserSubLevel = userSub.level.toString()
                            isEditDialogVisible = true
                        }
                    }
                }
            }
            is UserSubUiState.Error -> {
                Text(text = "Error loading subscriptions!")
            }
        }
        if (isAddDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    newUserSubName = ""
                    newUserSubLevel = ""
                    isAddDialogVisible = false },
                title = { Text("Create a subscription") },
                text = {
                    Column {
                        TextField(
                            value = newUserSubName,
                            onValueChange = { newUserSubName = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newUserSubLevel,
                            onValueChange = { newUserSubLevel = it },
                            label = { Text("Level") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newUserSubName.isNotEmpty() && newUserSubLevel.isNotEmpty()) {
                                userSubViewModel.createUserSub(newUserSubName, newUserSubLevel.toInt())
                                isAddDialogVisible = false
                                newUserSubName = ""
                                newUserSubLevel = ""
                            }
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            isAddDialogVisible = false
                            newUserSubName = ""
                            newUserSubLevel = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (isEditDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    isEditDialogVisible = false
                    updateUserSubId = ""
                    updateUserSubName = ""
                    updateUserSubLevel = "" },
                title = { Text("Edit subscription") },
                text = {
                    Column {
                        TextField(
                            value = updateUserSubName,
                            onValueChange = { updateUserSubName = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateUserSubLevel,
                            onValueChange = { updateUserSubLevel = it },
                            label = { Text("Level") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (updateUserSubName.isNotEmpty() && updateUserSubLevel.isNotEmpty()) {
                                userSubViewModel.updateUserSubById(updateUserSubId.toInt(), updateUserSubName, updateUserSubLevel.toInt())
                                isEditDialogVisible = false
                                updateUserSubId = ""
                                updateUserSubName = ""
                                updateUserSubLevel = ""
                            }
                        }
                    ) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            isEditDialogVisible = false
                            updateUserSubId = ""
                            updateUserSubName = ""
                            updateUserSubLevel = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun UserSubRow(userSub: UserSub, userSubViewModel: UserSubViewModel, onEditClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .clickable { expanded = true }
    ) {
        Text(userSub.name, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        UserSubDetailRow("Level:", userSub.level.toString())
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onEditClick()
            }) {
                Text("Edit")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                userSubViewModel.deleteUserSubById(userSub)
            }) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun UserSubDetailRow(title: String, value: String) {
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