package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.blitzware_android.model.License
import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.ui.viewmodels.LicenseUiState
import com.example.blitzware_android.ui.viewmodels.LicenseViewModel
import com.example.blitzware_android.ui.viewmodels.UserSubViewModel

/**
 * Licenses screen
 *
 * @param licenseViewModel
 * @param userSubViewModel
 */
@Composable
fun LicensesScreen(
    licenseViewModel: LicenseViewModel = viewModel(factory = LicenseViewModel.Factory),
    userSubViewModel: UserSubViewModel = viewModel(factory = UserSubViewModel.Factory),
) {
    val licenses by licenseViewModel.licenses.collectAsState()
    val userSubs by userSubViewModel.userSubs.collectAsState()
    var isAddDialogVisible by remember { mutableStateOf(false) }
    var isEditDialogVisible by remember { mutableStateOf(false) }

    var newLicenseDays by remember { mutableStateOf("") }
    var newLicenseFormat by remember { mutableStateOf("XXXXXXXXXXXXXXXXXXXX") }
    var newLicensePrefix by remember { mutableStateOf("") }
    var newLicenseAmount by remember { mutableStateOf("") }
    var subscription by remember { mutableIntStateOf(0) }
    val options = listOf(
        "XXXXXXXXXXXXXXXXXXXX",
        "PREFIX-XXXXXXXXXXXXXXXXXXXX",
        "XXXXX-XXXXX-XXXXX-XXXXX",
        "PREFIX-XXXXX-XXXXX-XXXXX-XXXXX"
    )

    var updateLicenseId by remember { mutableStateOf("") }
    var updateLicenseLicense by remember { mutableStateOf("") }
    var updateLicenseDays by remember { mutableStateOf("") }
    var updateLicenseUsed by remember { mutableStateOf("") }
    var updateLicenseEnabled by remember { mutableStateOf("") }
    var updateLicenseSubscription by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("License Manager - ${licenseViewModel.application.value?.name ?: "N/A"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    isEditDialogVisible = false
                    isAddDialogVisible = true
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(25, 118, 210)
                )
            }
        }

        when (licenseViewModel.licenseUiState) {
            is LicenseUiState.Loading -> {
                Text(text = "Loading licenses...")
            }
            is LicenseUiState.Success -> {
                LazyColumn {
                    items(licenses) { license ->
                        android.util.Log.d("LicensesScreen", "license: $license")
                        LicenseRow(license = license, userSubs = userSubs, licenseViewModel = licenseViewModel) {
                            isAddDialogVisible = false
                            updateLicenseId = license.id
                            updateLicenseLicense = license.license
                            updateLicenseDays = license.days.toString()
                            updateLicenseUsed = license.used.toString()
                            updateLicenseEnabled = license.enabled.toString()
                            updateLicenseSubscription = license.userSubId ?: 0
                            isEditDialogVisible = true
                        }
                    }
                }
            }
            is LicenseUiState.Error -> {
                Text(text = (licenseViewModel.licenseUiState as LicenseUiState.Error).message)
            }
        }

        if (isAddDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    newLicenseDays = ""
                    newLicenseFormat = "XXXXXXXXXXXXXXXXXXXX"
                    newLicensePrefix = ""
                    newLicenseAmount = ""
                    subscription = 0
                    isAddDialogVisible = false },
                title = { Text("Create licenses") },
                text = {
                    Column {
                        TextField(
                            value = newLicenseDays,
                            onValueChange = { newLicenseDays = it },
                            label = { Text("Duration (in days)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newLicenseAmount,
                            onValueChange = { newLicenseAmount = it },
                            label = { Text("Amount") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newLicenseDays.isNotEmpty() && newLicenseAmount.isNotEmpty()) {
                                licenseViewModel.createLicense(
                                    newLicenseDays.toInt(),
                                    newLicenseFormat,
                                    newLicenseAmount.toInt(),
                                    userSubs.first().id
                                )
                                isAddDialogVisible = false
                                newLicenseDays = ""
                                newLicenseFormat = "XXXXXXXXXXXXXXXXXXXX"
                                newLicenseAmount = ""
                                subscription = 0
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
                            newLicenseDays = ""
                            newLicenseFormat = "XXXXXXXXXXXXXXXXXXXX"
                            newLicensePrefix = ""
                            newLicenseAmount = ""
                            subscription = 0
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
                    updateLicenseId = ""
                    updateLicenseLicense = ""
                    updateLicenseDays = ""
                    updateLicenseUsed = ""
                    updateLicenseEnabled = ""
                    updateLicenseSubscription = -1 },
                title = { Text("Edit license") },
                text = {
                    Column {
                        TextField(
                            value = updateLicenseLicense,
                            onValueChange = { updateLicenseLicense = it },
                            label = { Text("License") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateLicenseDays,
                            onValueChange = { updateLicenseDays = it },
                            label = { Text("Days") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateLicenseUsed,
                            onValueChange = { updateLicenseUsed = it },
                            label = { Text("Used") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateLicenseEnabled,
                            onValueChange = { updateLicenseEnabled = it },
                            label = { Text("Enabled") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            userSubs.forEach { userSub ->
                                DropdownMenuItem(
                                    onClick = {
                                        subscription = userSub.id
                                    }
                                ) {
                                    Text(text = "${userSub.name} (${userSub.level})")
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (updateLicenseLicense.isNotEmpty() && updateLicenseDays.isNotEmpty() && updateLicenseUsed.isNotEmpty() && updateLicenseEnabled.isNotEmpty() && updateLicenseSubscription != -1) {
                                licenseViewModel.updateLicenseById(
                                    updateLicenseId,
                                    updateLicenseLicense,
                                    updateLicenseDays.toInt(),
                                    updateLicenseUsed.toInt(),
                                    updateLicenseEnabled.toInt(),
                                    updateLicenseSubscription
                                )
                                isEditDialogVisible = false
                                updateLicenseId = ""
                                updateLicenseLicense = ""
                                updateLicenseDays = ""
                                updateLicenseUsed = ""
                                updateLicenseEnabled = ""
                                updateLicenseSubscription = -1
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
                            updateLicenseId = ""
                            updateLicenseLicense = ""
                            updateLicenseDays = ""
                            updateLicenseUsed = ""
                            updateLicenseEnabled = ""
                            updateLicenseSubscription = -1
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

/**
 * License row
 *
 * @param license
 * @param userSubs
 * @param licenseViewModel
 * @param onEditClick
 * @receiver
 */
@Composable
fun LicenseRow(license: License, userSubs: List<UserSub>, licenseViewModel: LicenseViewModel, onEditClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var userSubString = ""
    for (userSub in userSubs) {
        if (userSub.id == license.userSubId) {
            userSubString = "${userSub.name} (${userSub.level})"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .clickable { expanded = true }
    ) {
        Text(license.license, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        LicenseDetailRow("Days:", license.days.toString())
        LicenseDetailRow("Subscription:", userSubString)
        LicenseDetailRow("Used:", if (license.used == 1) "Used" else "Not Used")
        (if (license.used == 1) license.usedBy else "N/A")?.let { LicenseDetailRow("Used By:", it) }
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
            if (license.enabled == 1) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    licenseViewModel.updateLicenseById2(license.copy(enabled = 0))
                }) {
                    Text("Ban")
                }
            } else {
                DropdownMenuItem(onClick = {
                    expanded = false
                    licenseViewModel.updateLicenseById2(license.copy(enabled = 1))
                }) {
                    Text("Unban")
                }
            }
            DropdownMenuItem(onClick = {
                expanded = false
                licenseViewModel.deleteLicenseById(license)
            }) {
                Text("Delete")
            }
        }
    }
}

/**
 * License detail row
 *
 * @param title
 * @param value
 */
@Composable
fun LicenseDetailRow(title: String, value: String) {
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