package com.nadhifhayazee.locationsaver.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nadhifhayazee.locationsaver.navigation.AppNavigation
import com.nadhifhayazee.locationsaver.navigation.HomeNavigation
import com.nadhifhayazee.locationsaver.navigation.LocationCreateNavigation
import com.nadhifhayazee.locationsaver.navigation.LocationDetailNavigation
import com.nadhifhayazee.locationsaver.screen.Route
import com.nadhifhayazee.locationsaver.screen.edit_note.EditNoteScreen
import com.nadhifhayazee.locationsaver.screen.edit_note.EditTitleScreen
import com.nadhifhayazee.locationsaver.screen.home.HomeScreen
import com.nadhifhayazee.locationsaver.screen.location_create.CreateLocationScreen
import com.nadhifhayazee.locationsaver.screen.location_detail.LocationDetailScreen


@Composable
fun LocationSaverApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    Scaffold() { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeNavigation.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            val homeNavigation = HomeNavigation(navController)

            composable(HomeNavigation.route) {
                HomeScreen(homeNavigation = homeNavigation)
            }

            composable(
                route = LocationDetailNavigation.route,
                arguments = listOf(navArgument(AppNavigation.LOCATION_ID_KEY) {
                    type = NavType.StringType
                })
            ) {
                val id = it.arguments?.getString(AppNavigation.LOCATION_ID_KEY, "")
                LocationDetailScreen(
                    locationDetailNavigation = LocationDetailNavigation(),
                    locationId = id
                )
            }


            composable(
                route = LocationCreateNavigation.route
            ) {
                CreateLocationScreen(navController = navController)
            }

            composable(
                route = Route.EditNote.route,
                arguments = listOf(navArgument("LOCATION_ID") {
                    type = NavType.IntType
                })
            ) {
                val id = it.arguments?.getInt("LOCATION_ID", 0)
                EditNoteScreen(locationId = id, navController = navController)

            }

            composable(
                route = Route.EditTitle.route,
                arguments = listOf(navArgument("LOCATION_ID") {
                    type = NavType.IntType
                })
            ) {
                val id = it.arguments?.getInt("LOCATION_ID", 0)
                EditTitleScreen(locationId = id, navController = navController)

            }


        }
    }

}