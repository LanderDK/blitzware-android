package com.example.blitzware_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blitzware_android.model.Account
import com.example.blitzware_android.ui.viewmodels.ChatMessageUiState
import com.example.blitzware_android.ui.viewmodels.ChatMessageViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CommunityScreen(
    chatMessageViewModel: ChatMessageViewModel = viewModel(factory = ChatMessageViewModel.Factory)
) {
    val chatMsgs by chatMessageViewModel.chatMsgs.collectAsState()
    val account by chatMessageViewModel.account.collectAsState()
    var msg by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Community General Chat",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { chatMessageViewModel.getChatMsgsByChatId(1) },
                modifier = Modifier.padding(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color(25, 118, 210)
                    )
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            when (chatMessageViewModel.chatMessageUiState) {
                is ChatMessageUiState.Loading -> {
                    Text(text = "Loading messages...")
                }
                is ChatMessageUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        reverseLayout = true,
                    ) {
                        items(chatMsgs.reversed()) { chatMsg ->
                            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(chatMsg.date)
                            ChatMsg(username = chatMsg.username, message = chatMsg.message, date = date!!, onDelete = {
                                chatMessageViewModel.deleteChatMessageById(chatMsg)
                            }, account = account ?: throw Exception("Account is null"))
                        }
                    }
                }
                is ChatMessageUiState.Error -> {
                    Text(text = (chatMessageViewModel.chatMessageUiState as ChatMessageUiState.Error).message)
                }
            }
        }
        OutlinedTextField(
            value = msg,
            onValueChange = { msg = it },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text("Type here, ${account?.account?.username}...")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (msg.isNotEmpty()) {
                        chatMessageViewModel.createChatMessage(msg, 1)
                        msg = ""
                    } else showAlert = true
                }
            ),
            trailingIcon = {
                if (msg.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            chatMessageViewModel.createChatMessage(msg, 1)
                            msg = ""
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    }
                }
            }
        )
    }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Oops!") },
            text = { Text("Please provide a valid message!") },
            confirmButton = {
                Button(
                    onClick = { showAlert = false },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ChatMsg(username: String, message: String, date: Date, onDelete: () -> Unit, account: Account) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .fillMaxWidth()
            .clickable {
                expanded = true
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = username,
                color = if (username == account.account.username) {
                    Color(28, 155, 239)
                } else {
                    Color(61, 83, 101)
                },
            )
            Text(
                text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date),
                color = Color.Gray,
            )
        }
        Text(
            text = message,
            modifier = Modifier
                .padding(10.dp)
                .background(
                    color = if (username == account.account.username) {
                        Color(28, 155, 239)
                    } else {
                        Color(61, 83, 101)
                    }
                )
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(min = 40.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            color = Color.White,
            maxLines = Int.MAX_VALUE
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDelete()
            }) {
                Text("Delete")
            }
        }
    }
}