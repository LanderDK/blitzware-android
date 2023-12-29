package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.blitzware_android.R
import com.example.blitzware_android.ui.viewmodels.AccountUiState
import com.example.blitzware_android.ui.viewmodels.AccountViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource

/**
 * Login screen
 *
 * @param accountViewModel
 */
@Composable
fun LoginScreen(accountViewModel: AccountViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var twoFactorCode by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val logoImage = painterResource(R.drawable.orangebwlogonobg512)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = logoImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_title_text),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.login_sub_title_text_1))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_sub_title_text_2),
                color = colorResource(R.color.main_orange),
                modifier = Modifier.clickable {
                    accountViewModel.registerScreen.value = true
                }
            )
        }

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
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    accountViewModel.login(username, password)
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.login_button_text))
        }

        accountViewModel.accountUiState.let { result ->
            val message = when (result) {
                is AccountUiState.Success -> "Login successful"
                is AccountUiState.Error -> result.message
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
                        Text(stringResource(R.string.login_button_text))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            twoFactorCode = ""
                        }
                    ) {
                        Text(stringResource(R.string.cancel_button_text))
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
                        Text(stringResource(R.string.login_button_text))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            otpCode = ""
                        }
                    ) {
                        Text(stringResource(R.string.cancel_button_text))
                    }
                }
            )
        }
    }
}

/**
 * Register screen
 *
 * @param accountViewModel
 */
@Composable
fun RegisterScreen(accountViewModel: AccountViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val logoImage = painterResource(R.drawable.orangebwlogonobg512)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = logoImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.register_title_text),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.register_sub_title_text_1))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.register_sub_title_text_2),
                color = colorResource(R.color.main_orange),
                modifier = Modifier.clickable {
                    accountViewModel.registerScreen.value = false
                }
            )
        }

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
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Mail, contentDescription = null)
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
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm password") },
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

        if (password != confirmPassword) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            onClick = {
                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                    accountViewModel.register(username, email, password)
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.register_button_text))
        }

        accountViewModel.accountUiState.let { result ->
            val message = when (result) {
                is AccountUiState.Success -> "Register successful"
                is AccountUiState.Error -> result.message
                is AccountUiState.Loading -> "Registering..."
            }

            Text(
                text = message,
                color = if (result is AccountUiState.Error) Color.Red else Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
