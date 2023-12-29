package com.example.blitzware_android.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blitzware_android.R
import com.example.blitzware_android.navigation.AppDetailBottomNavigation
import com.example.blitzware_android.navigation.NavigationDrawer
import com.example.blitzware_android.navigation.NavigationType
import com.example.blitzware_android.navigation.Screens
import com.example.blitzware_android.navigation.listOfMainNavItems
import com.example.blitzware_android.ui.screens.AccountDetailsScreen
import com.example.blitzware_android.ui.screens.AccountLogsScreen
import com.example.blitzware_android.ui.screens.AccountMenuScreen
import com.example.blitzware_android.ui.screens.AppDetailScreen
import com.example.blitzware_android.ui.screens.AppLogsScreen
import com.example.blitzware_android.ui.screens.AppsScreen
import com.example.blitzware_android.ui.screens.CommunityScreen
import com.example.blitzware_android.ui.screens.FilesScreen
import com.example.blitzware_android.ui.screens.LicensesScreen
import com.example.blitzware_android.ui.screens.ResourcesScreen
import com.example.blitzware_android.ui.screens.UserSubsScreen
import com.example.blitzware_android.ui.screens.UsersScreen

/**
 * Blitz ware app
 *
 * @param windowSize
 */
@Composable
fun BlitzWareApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController()
) {
    val selectedScreen by remember { mutableStateOf(Screens.AppDetailScreen) }

    val navigationType: NavigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            NavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            NavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            NavigationType.PERMANENT_NAVIGATION_DRAWER
        }
        else -> {
            NavigationType.BOTTOM_NAVIGATION
        }
    }

    when (navigationType) {
        NavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            // Permanent Navigation Drawer UI
            PermanentNavigationDrawer(drawerContent = {
                PermanentDrawerSheet(Modifier.width(dimensionResource(R.dimen.drawer_width))) {
                    NavigationDrawer(
                        selectedDestination = navController.currentDestination,
                        navController = navController,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }) {
                Scaffold(
                    containerColor = Color.Transparent,
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
                            LicensesScreen()
                        }
                        composable(route = Screens.UserSubsScreen.name) {
                            AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                            UserSubsScreen()
                        }
                        composable(route = Screens.FilesScreen.name) {
                            AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                            FilesScreen()
                        }
                        composable(route = Screens.AppLogsScreen.name) {
                            AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                            AppLogsScreen()
                        }

                        composable(route = Screens.AppSettingsScreen.name) {
                            AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                            Text(text = "App Settings")
                        }
                    }
                }
            }
        }
        NavigationType.BOTTOM_NAVIGATION -> {
            // Bottom Navigation UI
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
                        LicensesScreen()
                    }
                    composable(route = Screens.UserSubsScreen.name) {
                        AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                        UserSubsScreen()
                    }
                    composable(route = Screens.FilesScreen.name) {
                        AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                        FilesScreen()
                    }
                    composable(route = Screens.AppLogsScreen.name) {
                        AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                        AppLogsScreen()
                    }

                    composable(route = Screens.AppSettingsScreen.name) {
                        AppDetailBottomNavigation(navController = navController, selectedScreen = selectedScreen)
                        Text(text = "App Settings")
                    }
                }
            }
        }
        NavigationType.NAVIGATION_RAIL -> {
            // Navigation Rail UI
            Row {
                AnimatedVisibility(visible = true) {
                    NavigationRail {
                        com.example.blitzware_android.navigation.NavigationRail(navController = navController)
                    }
                }
                Scaffold(
                    containerColor = Color.Transparent,
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
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            AppDetailScreen(navController = navController)
                        }
                        composable(route = Screens.UsersScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            UsersScreen()
                        }
                        composable(route = Screens.LicensesScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            LicensesScreen()
                        }
                        composable(route = Screens.UserSubsScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            UserSubsScreen()
                        }
                        composable(route = Screens.FilesScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            FilesScreen()
                        }
                        composable(route = Screens.AppLogsScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            AppLogsScreen()
                        }

                        composable(route = Screens.AppSettingsScreen.name) {
                            AppDetailBottomNavigation(
                                navController = navController,
                                selectedScreen = selectedScreen
                            )
                            Text(text = "App Settings")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Get main navigation bar
 *
 * @param navController
 */
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
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorResource(R.color.light_orange)
                )
            )
        }
    }
}