package com.example.blitzware_android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
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