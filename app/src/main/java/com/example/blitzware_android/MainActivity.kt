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
import com.example.blitzware_android.ui.screens.AccountViewModel
import com.example.blitzware_android.ui.screens.LoginScreen
import com.example.blitzware_android.ui.theme.BlitzWareTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this, AccountViewModel.Factory).get(AccountViewModel::class.java)

        setContent {
            BlitzWareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(onLoginClick = { username, password ->
                        viewModel.login(username, password)
                    },
                        viewModel = viewModel
                        )
                }
            }
        }
    }
}
