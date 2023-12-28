package com.example.blitzware_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.blitzware_android.ui.BlitzWareApp
import com.example.blitzware_android.ui.screens.LoginScreen
import com.example.blitzware_android.ui.screens.RegisterScreen
import com.example.blitzware_android.ui.theme.BlitzWareTheme
import com.example.blitzware_android.ui.viewmodels.AccountViewModel

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : ComponentActivity() {

    private lateinit var accountViewModel: AccountViewModel

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        accountViewModel = ViewModelProvider(this, AccountViewModel.Factory).get(AccountViewModel::class.java)

        setContent {
            BlitzWareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (accountViewModel.isAuthed) {
                        val windowSize = calculateWindowSizeClass(this)
                        BlitzWareApp(windowSize.widthSizeClass)
                    } else {
                        if (accountViewModel.registerScreen.value) {
                            RegisterScreen(accountViewModel = accountViewModel)
                        } else {
                            LoginScreen(accountViewModel = accountViewModel)
                        }
                    }
                }
            }
        }
    }
}
