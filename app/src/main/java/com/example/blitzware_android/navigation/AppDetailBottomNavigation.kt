package com.example.blitzware_android.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * App detail bottom navigation
 *
 * @param navController
 * @param selectedScreen
 */
@Composable
fun AppDetailBottomNavigation(
    navController: NavHostController,
    selectedScreen: Screens
) {
    if (shouldShowCustomNavigation(screen = selectedScreen)) {
        GetAppDetailNavigationBar(navController = navController)
    }
}

/**
 * Get app detail navigation bar
 *
 * @param navController
 */
@Composable
fun GetAppDetailNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        listOfAppDetailNavItems.forEach { navItem ->
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
                /*label = {
                    Text(text = navItem.label)
                }*/
            )
        }
    }
}

/**
 * Should show custom navigation
 *
 * @param screen
 * @return
 */
@Composable
fun shouldShowCustomNavigation(screen: Screens): Boolean {
    return screen == Screens.AppDetailScreen ||
            screen == Screens.UsersScreen ||
            screen == Screens.LicensesScreen ||
            screen == Screens.UserSubsScreen ||
            screen == Screens.FilesScreen ||
            screen == Screens.AppLogsScreen
}