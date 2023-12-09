package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blitzware_android.model.Application

@Composable
fun AppsScreen(
    accountViewModel: AccountViewModel,
    applicationViewModel: ApplicationViewModel
) {
    val applications by applicationViewModel.applications.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome back, ${accountViewModel.account!!.account.username}!",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { /*TODO*/ },
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
                            ApplicationItem(application = application)
                            //Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                is ApplicationUiState.Error -> {
                    Text(text = "Error loading applications!")
                }
            }
        }
    }
}

@Composable
fun ApplicationItem(application: Application) {
    Column(
        modifier = Modifier.padding(16.dp),
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
    }
}
