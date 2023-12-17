package com.example.blitzware_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blitzware_android.ui.screens.AccountDetailsScreen
import com.example.blitzware_android.ui.screens.AccountLogsScreen
import com.example.blitzware_android.ui.screens.AccountMenuScreen
import com.example.blitzware_android.ui.screens.AppDetailScreen
import com.example.blitzware_android.ui.screens.AppsScreen
import com.example.blitzware_android.ui.screens.CommunityScreen
import com.example.blitzware_android.ui.screens.ResourcesScreen
import com.example.blitzware_android.ui.screens.UsersScreen

@Composable
fun MainBottomNavigation() {
    val navController: NavHostController = rememberNavController()
    var selectedScreen by remember { mutableStateOf(Screens.AppDetailScreen) }

    Scaffold(
        bottomBar = {
            GetMainNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.AppsScreen.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.AppsScreen.name) {
                AppsScreen(navController = navController)
            }
            composable(route = Screens.ResourcesScreen.name) {
                ResourcesScreen()
            }
            composable(route = Screens.CommunityScreen.name) {
                CommunityScreen()
            }
            composable(route = Screens.AccountMenuScreen.name) {
                AccountMenuScreen(navController = navController)
            }
            composable(route = Screens.AccountLogsScreen.name) {
                AccountLogsScreen(navController = navController)
            }
            composable(route = Screens.AccountDetailScreen.name) {
                AccountDetailsScreen(navController = navController)
            }
            composable(route = Screens.AppDetailScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                AppDetailScreen(navController = navController)
            }
            composable(route = Screens.UsersScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                UsersScreen()
            }
            composable(route = Screens.LicensesScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                Text(text = "LicensesScreen") //LicensesScreen()
            }
            composable(route = Screens.UserSubsScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                Text(text = "UserSubsScreen") //UserSubsScreen()
            }
            composable(route = Screens.FilesScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                Text(text = "FilesScreen") //FilesScreen()
            }
            composable(route = Screens.AppLogsScreen.name) {
                AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                Text(text = "AppLogsScreen") //AppLogsScreen()
            }
        }
    }

}

@Composable
fun GetMainNavigationBar(navController: NavHostController) {
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