package com.example.blitzware_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blitzware_android.ui.screens.AccountViewModel
import com.example.blitzware_android.ui.screens.ApplicationViewModel
import com.example.blitzware_android.ui.screens.AppsScreen

@Composable
fun MainBottomNavigation(accountViewModel: AccountViewModel) {
    val navController: NavHostController = rememberNavController()
    val applicationViewModel = ApplicationViewModel(accountViewModel)

    Scaffold(
        bottomBar = {
            GetNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.AppsScreen.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.AppsScreen.name) {
                AppsScreen(accountViewModel = accountViewModel, applicationViewModel = applicationViewModel)
            }
            composable(route = Screens.ResourcesScreen.name) {
                //ResourcesScreen()
            }
            composable(route = Screens.CommunityScreen.name) {
                //CommunityScreen()
            }
            composable(route = Screens.AccountMenuScreen.name) {
                //AccountMenuScreen()
            }
        }
    }

}

@Composable
fun GetNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        listOfMainNavItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = null)
                },
                label = {
                    Text(text = navItem.label)
                }
            )
        }
    }
}