package com.nadhifhayazee.locationsaver.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nadhifhayazee.locationsaver.screen.home.HomeScreen
import com.nadhifhayazee.locationsaver.screen.location_detail.LocationDetailScreen


open class AppNavigation(
    val navHostController: NavHostController
) {

    val navBackStackEntry get() = navHostController.currentBackStackEntry
    val currentRoute = navBackStackEntry?.destination?.route


    fun create(navGraphBuilder: NavGraphBuilder) {
//        navGraphBuilder.composable(HomeNavigation.route) {
//            HomeScreen(homeNavigation = HomeNavigation(navHostController))
//        }
//
//        navGraphBuilder.composable(
//            LocationDetailNavigation.route, arguments = listOf(navArgument(
//                LOCATION_ID_KEY
//            ) { type = NavType.StringType })
//        ) {
//            LocationDetailScreen(locationDetailNavigation = LocationDetailNavigation())
//        }

    }

    companion object {
        const val LOCATION_ID_KEY = "LOCATION_ID_KEY"
    }
}