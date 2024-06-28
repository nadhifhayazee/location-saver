package com.nadhifhayazee.locationsaver.base

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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

            composable(HomeNavigation.route,
//                exitTransition = {
//                    return@composable slideOutOfContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
//                    )
//                }, popExitTransition = {
//                    return@composable fadeOut(tween(700))
//                },
//                popExitTransition = {
//                    slideOutOfContainer(
//                        animationSpec = tween(300, easing = EaseOut),
//                        towards = AnimatedContentTransitionScope.SlideDirection.End
//                    )
//                },
//                popEnterTransition = {
//                    return@composable slideIntoContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Start,
//                        tween(700)
//                    )
//                },

                enterTransition = {
                    return@composable fadeIn(tween(1000))
                }, exitTransition = {
                    return@composable fadeOut(tween(700))
                }, popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                    )
                }
            ) {
                HomeScreen(navController = navController)
            }


            composable(
                route = LocationCreateNavigation.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                    )
                },
//                exitTransition = {
//                    slideOutOfContainer(
//                        animationSpec = tween(300, easing = EaseOut),
//                        towards = AnimatedContentTransitionScope.SlideDirection.End
//                    )
//                },
            ) {
                CreateLocationScreen(navController = navController)
            }

            composable(
                route = Route.EditNote.route,
                arguments = listOf(navArgument("LOCATION_ID") {
                    type = NavType.IntType
                },),
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                    )
                },
            ) {
                val id = it.arguments?.getInt("LOCATION_ID", 0)
                EditNoteScreen(locationId = id, navController = navController)

            }

            composable(
                route = Route.EditTitle.route,
                arguments = listOf(navArgument("LOCATION_ID") {
                    type = NavType.IntType
                }),
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                    )
                },
            ) {
                val id = it.arguments?.getInt("LOCATION_ID", 0)
                EditTitleScreen(locationId = id, navController = navController)

            }


        }
    }

}