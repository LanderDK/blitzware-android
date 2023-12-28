package com.example.blitzware_android.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.blitzware_android.R

@Composable
fun NavigationDrawer(
    selectedDestination: NavDestination?,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Column(modifier = modifier) {
        for (navItem in listOfMainNavItems) {
            NavigationDrawerItem(
                selected = selectedDestination?.hierarchy?.any { it.route == navItem.route } == true,
                label = {
                    Text(
                        text = navItem.label,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header)),
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label,
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                ),
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
