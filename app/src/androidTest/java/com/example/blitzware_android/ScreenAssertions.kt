package com.example.blitzware_android

import androidx.navigation.NavController
import org.junit.Assert

/**
 * Assert current route name
 *
 * @param expectedRouteName
 */
fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}