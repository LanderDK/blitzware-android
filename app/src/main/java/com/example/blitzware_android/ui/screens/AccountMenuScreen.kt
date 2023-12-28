package com.example.blitzware_android.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.blitzware_android.MainActivity
import com.example.blitzware_android.ui.viewmodels.AccountViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountMenuScreen(navController: NavHostController) {
    val accountViewModel: AccountViewModel = viewModel(factory = AccountViewModel.Factory)
    var showingLogoutAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Account Settings",
                    fontSize = 20.sp,
                )
                AccountMenuItem(
                    icon = Icons.Default.Receipt,
                    text = "Account Logs",
                    onClick = { navController.navigate("AccountLogsScreen") }
                )

                AccountMenuItem(
                    icon = Icons.Default.Person,
                    text = "Account Details",
                    onClick = { navController.navigate("AccountDetailScreen") }
                )

                AccountMenuItem(
                    icon = Icons.Default.AdminPanelSettings,
                    text = "Admin Panel",
                    onClick = { /*navController.navigate("adminPanel")*/ }
                )

                Spacer(modifier = Modifier.weight(1f))

                LogoutButton(onClick = { showingLogoutAlert = true })
            }
        }
    )

    if (showingLogoutAlert) {
        LogoutAlert(
            onConfirm = {
                showingLogoutAlert = false
                accountViewModel.logout()
                resetApp(context = context)
            },
            onDismiss = {
                showingLogoutAlert = false
            }
        )
    }
}

@Composable
fun AccountMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Logout")
    }
}

@Composable
fun LogoutAlert(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Logout") },
        text = { Text("Are you sure you want to logout?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}


fun resetApp(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}