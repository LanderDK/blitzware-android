package com.example.blitzware_android.ui.screens

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.blitzware_android.R
import com.example.blitzware_android.model.ProfilePicture
import com.example.blitzware_android.model.UpdateAccountPicBody
import com.example.blitzware_android.ui.viewmodels.AccountUiState
import com.example.blitzware_android.ui.viewmodels.AccountViewModel
import org.json.JSONObject

/**
 * Account details screen
 *
 * @param accountViewModel
 * @param navController
 */
@Composable
fun AccountDetailsScreen(
    accountViewModel: AccountViewModel = viewModel(factory = AccountViewModel.Factory),
    navController: NavHostController
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var imageSelected by remember { mutableStateOf(false) }
    val contentResolver = LocalContext.current.contentResolver
    var imageData: UpdateAccountPicBody by remember { mutableStateOf(UpdateAccountPicBody(ProfilePicture("", "", 0, ""))) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
            if (selectedImageUri != null) {
                imageData = convertImageToDataUrl(selectedImageUri!!, contentResolver)
                imageSelected = true
            }
        }
    )

    val scrollState = rememberScrollState()

    LaunchedEffect(accountViewModel) {
        accountViewModel.getAccountById()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color(25, 118, 210)
                    )
                }
            )
            Text(
                text = stringResource(R.string.account_details_title),
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        when (accountViewModel.accountUiState) {
            is AccountUiState.Loading -> {
                Text(text = "Loading account...")
            }
            is AccountUiState.Success -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePicture(accountViewModel = accountViewModel)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(8.dp),
                        ) {
                            Text(text = "Change photo")
                        }
                        Button(
                            onClick = {
                                accountViewModel.updateAccountProfilePictureById(imageData)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(8.dp),
                            enabled = imageSelected
                        ) {
                            Text(text = "Upload")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AccountInfoItemView(icon = Icons.Default.Key, text = accountViewModel.account?.account?.id ?: "")
                AccountInfoItemView(icon = Icons.Default.Person, text = accountViewModel.account?.account?.username ?: "")
                AccountInfoItemView(icon = Icons.Default.Mail, text = accountViewModel.account?.account?.email ?: "")
                AccountInfoItemView(icon = rolesIcon(accountViewModel), text = accountViewModel.account?.account?.roles?.get(0) ?: "")
            }
            is AccountUiState.Error -> {
                Text(text = (accountViewModel.accountUiState as AccountUiState.Error).message)
            }
        }
    }
}

/**
 * Profile picture
 *
 * @param accountViewModel
 */
@Composable
fun ProfilePicture(accountViewModel: AccountViewModel) {
    val defaultImage: Painter = painterResource(id = R.drawable.avatar)
    var profileImage: Painter by remember { mutableStateOf(defaultImage) }

    val profilePicture = accountViewModel.account?.account?.profilePicture
    if (profilePicture != null) {
        val json = JSONObject(profilePicture)
        val dataURL = json.optString("dataURL", "")
        val base64Data = dataURL.substringAfter(",").trim()
        val decodedData = Base64.decode(base64Data, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedData, 0, decodedData.size)
        profileImage = rememberAsyncImagePainter(model = bitmap)
    }

    Image(
        painter = profileImage,
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
    )
}

/**
 * Account info item view
 *
 * @param icon
 * @param text
 */
@Composable
fun AccountInfoItemView(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Text(text = text)
    }
}

/**
 * Roles icon
 *
 * @param accountViewModel
 * @return
 */
@Composable
fun rolesIcon(accountViewModel: AccountViewModel): ImageVector {
    return when (accountViewModel.account?.account?.roles?.get(0)) {
        "admin" -> Icons.Default.AdminPanelSettings
        "basic" -> Icons.Default.Battery4Bar
        "pro" -> Icons.Default.Battery5Bar
        "enterprise" -> Icons.Default.BatteryFull
        "free" -> Icons.Default.Battery2Bar
        else -> Icons.Default.Battery2Bar
    }
}

/**
 * Convert image to data url
 *
 * @param imageUri
 * @param contentResolver
 * @return
 */
@SuppressLint("Recycle")
fun convertImageToDataUrl(imageUri: Uri, contentResolver: ContentResolver): UpdateAccountPicBody {
    val inputStream = contentResolver.openInputStream(imageUri)
    val byteArray = inputStream?.readBytes()
    val base64Data = Base64.encodeToString(byteArray, Base64.DEFAULT)
    return UpdateAccountPicBody(
        profilePicture = ProfilePicture(
            name = imageUri.lastPathSegment ?: "",
            type = contentResolver.getType(imageUri) ?: "",
            size = byteArray?.size ?: 0,
            dataURL = "data:${contentResolver.getType(imageUri)};base64,$base64Data"
        )
    )
}
