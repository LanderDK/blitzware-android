package com.example.blitzware_android.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResourcesScreen() {
    Column {
        Text(
            text = "Available Resources",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn {
            item {
                ResourceItem(
                    name = "API Documentation",
                    link = "https://docs.blitzware.xyz/",
                    iconName = "arrowshape.forward.fill"
                )
                ResourceItem(
                    name = "Video Tutorials",
                    link = "https://youtube.com/playlist?list=PLdX34hqAHqx8eia7qY4XE8R68t769vA-j&si=2fGZaEvYzm8x-byx",
                    iconName = "arrowshape.forward.fill"
                )
                ResourceItem(
                    name = "C# Example",
                    link = "https://github.com/LanderDK/BlitzWare-CSHARP-Example",
                    iconName = "arrow.down.to.line"
                )
                ResourceItem(
                    name = "C++ Example",
                    link = "https://github.com/LanderDK/BlitzWare-CPP-Example",
                    iconName = "arrow.down.to.line"
                )
                ResourceItem(
                    name = "Python Example",
                    link = "https://github.com/LanderDK/BlitzWare-Python-Example",
                    iconName = "arrow.down.to.line"
                )
                ResourceItem(
                    name = "Java Example",
                    link = "https://github.com/LanderDK/ApiTestJava",
                    iconName = "arrow.down.to.line"
                )
                ResourceItem(
                    name = "ReactJS Example",
                    link = "https://github.com/LanderDK/ApiTestReact",
                    iconName = "arrow.down.to.line"
                )
            }
        }
    }
}

@Composable
fun ResourceItem(name: String, link: String, iconName: String) {
    val density = LocalDensity.current.density
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(White)
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = link,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = {
                    try {
                        uriHandler.openUri(link)
                    } catch (e: Exception) {
                        Log.d("ResourcesScreen", e.message.toString())
                    }
                },
                modifier = Modifier
                    .size((20 * density).dp)
            ) {
                Icon(
                    imageVector = when (iconName) {
                        "arrowshape.forward.fill" -> Icons.Default.ArrowForward
                        "arrow.down.to.line" -> Icons.Default.ArrowDownward
                        else -> Icons.Default.Link
                    },
                    contentDescription = null,
                    tint = Color(253, 126, 20)
                )
            }
        }
    }
}