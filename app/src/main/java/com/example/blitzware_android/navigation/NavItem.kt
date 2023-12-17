package com.example.blitzware_android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val listOfMainNavItems : List<NavItem> = listOf(
    NavItem(
        label = "Apps",
        icon = Icons.Default.Home,
        route = Screens.AppsScreen.name
    ),
    NavItem(
        label = "Resources",
        icon = Icons.Default.MenuBook,
        route = Screens.ResourcesScreen.name
    ),
    NavItem(
        label = "Community",
        icon = Icons.Default.Groups,
        route = Screens.CommunityScreen.name
    ),
    NavItem(
        label = "Account",
        icon = Icons.Default.Person,
        route = Screens.AccountMenuScreen.name
    )
)

val listOfAppDetailNavItems : List<NavItem> = listOf(
    NavItem(
        label = "Panel",
        icon = Icons.Default.GridView,
        route = Screens.AppDetailScreen.name
    ),
    NavItem(
        label = "Users",
        icon = Icons.Default.Person,
        route = Screens.UsersScreen.name
    ),
    NavItem(
        label = "Licenses",
        icon = Icons.Default.Key,
        route = Screens.LicensesScreen.name
    ),
    NavItem(
        label = "Subscriptions",
        icon = Icons.Default.CardGiftcard,
        route = Screens.UserSubsScreen.name
    ),
    NavItem(
        label = "Files",
        icon = Icons.Default.AttachFile,
        route = Screens.FilesScreen.name
    ),
    NavItem(
        label = "App Logs",
        icon = Icons.Default.ListAlt,
        route = Screens.AppLogsScreen.name
    ),
    NavItem(
        label = "App Settings",
        icon = Icons.Default.Settings,
        route = Screens.AppSettingsScreen.name)
)