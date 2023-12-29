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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.blitzware_android.model.File
import com.example.blitzware_android.ui.viewmodels.FileUiState
import com.example.blitzware_android.ui.viewmodels.FileViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

/**
 * Files screen
 *
 * @param fileViewModel
 */
@Composable
fun FilesScreen(fileViewModel: FileViewModel = viewModel(factory = FileViewModel.Factory)) {
    val files by fileViewModel.files.collectAsState()

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
            Text("File Manager - ${fileViewModel.application.value?.name ?: "N/A"}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {},
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(25, 118, 210)
                )
            }
        }

        when (fileViewModel.fileUiState) {
            is FileUiState.Loading -> {
                Text(text = "Loading files...")
            }
            is FileUiState.Success -> {
                LazyColumn {
                    items(files) { file ->
                        FileRow(file = file, fileViewModel = fileViewModel)
                    }
                }
            }
            is FileUiState.Error -> {
                Text(text = (fileViewModel.fileUiState as FileUiState.Error).message)
            }
        }
    }
}

/**
 * File row
 *
 * @param file
 * @param fileViewModel
 */
@Composable
fun FileRow(file: File, fileViewModel: FileViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val createdOn = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(file.createdOn)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
            .clickable { expanded = true }
    ) {
        Text(file.name, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
        )
        FileDetailRow("ID:", file.id)
        FileDetailRow("Size:", formatBytes(file.size) ?: "N/A")
        FileDetailRow("Created On:", SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(createdOn!!))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                fileViewModel.deleteFileById(file)
            }) {
                Text("Delete")
            }
        }
    }
}

/**
 * File detail row
 *
 * @param title
 * @param value
 */
@Composable
fun FileDetailRow(title: String, value: String) {
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

/**
 * Format bytes
 *
 * @param bytesString
 * @param decimals
 * @return
 */
fun formatBytes(bytesString: String, decimals: Int = 2): String? {
    val bytes = bytesString.toDoubleOrNull() ?: return null

    if (bytes == 0.0) {
        return "0 Bytes"
    }

    val k = 1024.0
    val dm = if (decimals < 0) 0 else decimals
    val sizes = listOf("Bytes", "KB", "MB", "GB", "TB", "PB")
    val i = floor(log(bytes, k)).toInt()

    return String.format("%.${dm}f %s", bytes / k.pow(i.toDouble()), sizes[i])
}