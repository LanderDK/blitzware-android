package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.blitzware_android.ui.viewmodels.AccountUiState
import com.example.blitzware_android.ui.viewmodels.AccountViewModel

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit, accountViewModel: AccountViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var twoFactorCode by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
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

        Button(
            onClick = { onLoginClick(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Login")
        }

        accountViewModel.accountUiState.let { result ->
            val message = when (result) {
                is AccountUiState.Success -> "Login successful"
                is AccountUiState.Error -> "Login failed"
                is AccountUiState.Loading -> "Logging in..."
            }

            Text(
                text = message,
                color = if (result is AccountUiState.Error) Color.Red else Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (accountViewModel.twoFactorRequired) {
            AlertDialog(
                onDismissRequest = { twoFactorCode = "" },
                title = { Text("Two-Factor Authentication") },
                text = {
                    TextField(
                        value = twoFactorCode,
                        onValueChange = { twoFactorCode = it },
                        label = { Text("6-digit code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (twoFactorCode.isNotBlank()) {
                                accountViewModel.verifyLogin2FA(username, twoFactorCode)
                                twoFactorCode = ""
                            }
                        }
                    ) {
                        Text("Login")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            twoFactorCode = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (accountViewModel.otpRequired) {
            AlertDialog(
                onDismissRequest = { otpCode = "" },
                title = { Text("Action required: check email") },
                text = {
                    TextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("4-digit code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (otpCode.isNotBlank()) {
                                accountViewModel.verifyLoginOTP(username, otpCode)
                                otpCode = ""
                            }
                        }
                    ) {
                        Text("Login")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            otpCode = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
