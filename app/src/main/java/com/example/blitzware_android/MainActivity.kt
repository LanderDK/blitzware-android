package com.example.blitzware_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.blitzware_android.navigation.MainBottomNavigation
import com.example.blitzware_android.ui.viewmodels.AccountViewModel
import com.example.blitzware_android.ui.screens.LoginScreen
import com.example.blitzware_android.ui.screens.RegisterScreen
import com.example.blitzware_android.ui.theme.BlitzWareTheme

class MainActivity : ComponentActivity() {

    private lateinit var accountViewModel: AccountViewModel

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
                        MainBottomNavigation()
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
