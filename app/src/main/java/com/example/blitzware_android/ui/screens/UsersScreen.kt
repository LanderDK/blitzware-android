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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blitzware_android.model.User
import com.example.blitzware_android.model.UserSub
import com.example.blitzware_android.ui.viewmodels.UserSubViewModel
import com.example.blitzware_android.ui.viewmodels.UserUiState
import com.example.blitzware_android.ui.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

/**
 * Users screen
 *
 * @param userViewModel
 * @param userSubViewModel
 */
@Composable
fun UsersScreen(
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
    userSubViewModel: UserSubViewModel = viewModel(factory = UserSubViewModel.Factory),
) {
    val users by userViewModel.users.collectAsState()
    val userSubs by userSubViewModel.userSubs.collectAsState()
    var isAddDialogVisible by remember { mutableStateOf(false) }
    var isEditDialogVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    var newUserName by remember { mutableStateOf("") }
    var newUserEmail by remember { mutableStateOf("") }
    var newUserPassword by remember { mutableStateOf("") }
    var newUserExpiry by remember { mutableStateOf(LocalDateTime.now().plusDays(1)) }
    var subscription by remember { mutableIntStateOf(0) }

    var updateUserId by remember { mutableStateOf("") }
    var updateUserName by remember { mutableStateOf("") }
    var updateUserEmail by remember { mutableStateOf("") }
    var updateUserExpiry by remember { mutableStateOf("") }
    var updateUserHwid by remember { mutableStateOf("") }
    var updateUserTwoFactorAuth by remember { mutableStateOf("") }
    var updateUserEnabled by remember { mutableStateOf("") }
    var updateUserSubscription by remember { mutableIntStateOf(-1) }

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
            Text("User Manager - ${userViewModel.application.value?.name ?: "N/A"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
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

        when (userViewModel.userUiState) {
            is UserUiState.Loading -> {
                Text(text = "Loading users...")
            }
            is UserUiState.Success -> {
                LazyColumn {
                    items(users) { user ->
                        UserRow(user = user, userSubs = userSubs, userViewModel = userViewModel) {
                            isAddDialogVisible = false
                            updateUserId = user.id
                            updateUserName = user.username
                            updateUserEmail = user.email
                            updateUserExpiry = user.expiryDate
                            updateUserHwid = user.hwid
                            updateUserTwoFactorAuth = user.twoFactorAuth.toString()
                            updateUserEnabled = user.enabled.toString()
                            updateUserSubscription = user.userSubId ?: 0
                            isEditDialogVisible = true
                        }
                    }
                }
            }
            is UserUiState.Error -> {
                Text(text = (userViewModel.userUiState as UserUiState.Error).message)
            }
        }

        if (isAddDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    newUserName = ""
                    newUserEmail = ""
                    newUserPassword = ""
                    newUserExpiry = null
                    subscription = 0
                    isAddDialogVisible = false },
                title = { Text("Create an user") },
                text = {
                    Column {
                        TextField(
                            value = newUserName,
                            onValueChange = { newUserName = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newUserEmail,
                            onValueChange = { newUserEmail = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = newUserPassword,
                            onValueChange = { newUserPassword = it },
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Password
                            ),
                            modifier = Modifier.fillMaxWidth(),

                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                // Please provide localized description for accessibility services
                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, description)
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newUserName.isNotEmpty() && newUserEmail.isNotEmpty() && newUserPassword.isNotEmpty()) {
                                userViewModel.createUserFromDashboard(newUserName, newUserEmail, newUserPassword, userSubs.first().id)
                                isAddDialogVisible = false
                                newUserName = ""
                                newUserEmail = ""
                                newUserPassword = ""
                                newUserExpiry = null
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
                            newUserName = ""
                            newUserEmail = ""
                            newUserPassword = ""
                            newUserExpiry = null
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
                    updateUserId = ""
                    updateUserName = ""
                    updateUserEmail = ""
                    updateUserExpiry = ""
                    updateUserHwid = ""
                    updateUserTwoFactorAuth = ""
                    updateUserEnabled = ""
                    updateUserSubscription = -1 },
                title = { Text("Edit user") },
                text = {
                    Column {
                        TextField(
                            value = updateUserName,
                            onValueChange = { updateUserName = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateUserEmail,
                            onValueChange = { updateUserEmail = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateUserHwid,
                            onValueChange = { updateUserHwid = it },
                            label = { Text("HWID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateUserTwoFactorAuth,
                            onValueChange = { updateUserTwoFactorAuth = it },
                            label = { Text("2FA") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = updateUserEnabled,
                            onValueChange = { updateUserEnabled = it },
                            label = { Text("Enabled") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (updateUserName.isNotEmpty() && updateUserEmail.isNotEmpty() && updateUserHwid.isNotEmpty() && updateUserTwoFactorAuth.isNotEmpty() && updateUserEnabled.isNotEmpty()) {
                                userViewModel.updateUserById(
                                    updateUserId,
                                    updateUserName,
                                    updateUserEmail,
                                    updateUserExpiry,
                                    updateUserHwid,
                                    updateUserTwoFactorAuth.toInt(),
                                    updateUserEnabled.toInt(),
                                    updateUserSubscription
                                    )
                                isEditDialogVisible = false
                                updateUserId = ""
                                updateUserName = ""
                                updateUserEmail = ""
                                updateUserExpiry = ""
                                updateUserHwid = ""
                                updateUserTwoFactorAuth = ""
                                updateUserEnabled = ""
                                updateUserSubscription = -1
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
                            updateUserId = ""
                            updateUserName = ""
                            updateUserEmail = ""
                            updateUserExpiry = ""
                            updateUserHwid = ""
                            updateUserTwoFactorAuth = ""
                            updateUserEnabled = ""
                            updateUserSubscription = -1
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
 * User row
 *
 * @param user
 * @param userSubs
 * @param userViewModel
 * @param onEditClick
 * @receiver
 */
@Composable
fun UserRow(user: User, userSubs: List<UserSub>, userViewModel: UserViewModel, onEditClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val expiryDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(user.expiryDate)
    val lastLogin = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(user.lastLogin)
    var userSubString = ""
    for (userSub in userSubs) {
        if (userSub.id == user.userSubId) {
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
        Text(user.username, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        UserDetailRow("Email:", user.email)
        UserDetailRow("Expiry:", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(expiryDate!!))
        UserDetailRow("Subscription:", userSubString)
        UserDetailRow("Last Login:", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(lastLogin!!))
        UserDetailRow("IP:", user.lastIP)
        UserDetailRow("HWID:", user.hwid)
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
                userViewModel.updateUserById2(user.copy(hwid = "RESET"))
            }) {
                Text("Reset HWID")
            }
            if (user.enabled == 1) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    userViewModel.updateUserById2(user.copy(enabled = 0))
                }) {
                    Text("Ban")
                }
            } else {
                DropdownMenuItem(onClick = {
                    expanded = false
                    userViewModel.updateUserById2(user.copy(enabled = 1))
                }) {
                    Text("Unban")
                }
            }
            DropdownMenuItem(onClick = {
                expanded = false
                userViewModel.deleteUserById(user)
            }) {
                Text("Delete")
            }
        }
    }
}

/**
 * User detail row
 *
 * @param title
 * @param value
 */
@Composable
fun UserDetailRow(title: String, value: String) {
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