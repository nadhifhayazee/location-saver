package com.nadhifhayazee.locationsaver.screen.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.locationsaver.R
import com.nadhifhayazee.locationsaver.navigation.LocationCreateNavigation
import com.nadhifhayazee.locationsaver.screen.Route
import com.nadhifhayazee.locationsaver.screen.home.component.LocationItem
import com.nadhifhayazee.locationsaver.tools.ImplicitIntent
import com.nadhifhayazee.shared.tools.ImageUriGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val locationsState by homeViewModel.myLocations.collectAsState()

    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    var uri: Uri? = null
    var selectedLocation: Location? = null
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {

            if (it) {
                selectedLocation?.id?.let { id ->
                    homeViewModel.addLocationImage(
                        uri.toString(),
                        id
                    )
                }

            }
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            uri = ImageUriGenerator.createImageUri(context)
            cameraLauncher.launch(uri!!)
        } else {
            renderSnackBar(
                message = "This app needs camera permission to function properly. Please grant the permission in app settings.",
                snackBarHostState = snackBarHostState,
                coroutineScope = coroutineScope,
                context
            )
        }
    }

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val launcherLocationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            navController?.navigate(LocationCreateNavigation.route)
        } else {
            renderSnackBar(
                message = "This app needs location permission to function properly. Please grant the permission in app settings.",
                snackBarHostState = snackBarHostState,
                coroutineScope = coroutineScope,
                context
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            AddLocationFloatingButton(
                onAddLocationClicked = {
                    if (locationPermissions.all {
                            ContextCompat.checkSelfPermission(
                                context,
                                it
                            ) == PackageManager.PERMISSION_GRANTED
                        }) {
                        navController?.navigate(LocationCreateNavigation.route)
                    } else {
                        launcherLocationPermission.launch(locationPermissions)
                    }
                },
            )
        },
    ) { paddingValues ->


        HomeScreenContent(
            modifier = modifier,
            locationsState = locationsState,
            navController,
            paddingValues,
            onDeleteLocation = {
                homeViewModel.onEvent(LocationEvent.DeleteLocation(it.id.toString()))
            },
            onAddImage = { location ->
                selectedLocation = location
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    uri = ImageUriGenerator.createImageUri(context)
                    cameraLauncher.launch(uri!!)
                } else {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        )
    }


}

fun renderSnackBar(
    message: String,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    context: Context
) {
    coroutineScope.launch {
        val action = snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
            actionLabel = "Setting",
            withDismissAction = true
        )

        when (action) {
            SnackbarResult.Dismissed -> {

            }

            SnackbarResult.ActionPerformed -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun AddLocationFloatingButton(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    onAddLocationClicked: () -> Unit,
) {


    FloatingActionButton(onClick = {
        onAddLocationClicked()
    }, shape = CircleShape, modifier = modifier.size(55.dp)) {
        Image(
            painter = painterResource(
                id = if (darkTheme) R.drawable.ic_add_location_dark_theme
                else R.drawable.ic_add_location_light_theme
            ),
            contentDescription = ""
        )
    }
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    locationsState: LocationState,
    navController: NavHostController?,
    paddingValues: PaddingValues = PaddingValues(),
    onDeleteLocation: (Location) -> Unit,
    onAddImage: (Location) -> Unit
) {

//    val progressAnimate by animateFloatAsState(targetValue = )
    Log.d("HomeScreenContent", "HomeScreenContent: $locationsState")
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


//        if (!locationsState.loading && locationsState.locations?.isEmpty() == true) {
//            DataNotExist()
//        } else if (!locationsState.loading && locationsState.locations?.isNotEmpty() == true) {
//
//        }

        AnimatedVisibility(visible = !locationsState.loading && locationsState.locations?.isEmpty() == true) {
            DataNotExist()
        }

        AnimatedVisibility(visible = !locationsState.loading && locationsState.locations?.isNotEmpty() == true) {
            LocationList(
                locations = locationsState.locations,
                navHostController = navController,
                onDeleteLocation = { location ->
                    onDeleteLocation(location)
                },
                onAddImage = { location ->
                    onAddImage(location)
                }
            )
        }
        AnimatedVisibility(

            visible = locationsState.loading,
            exit = shrinkOut()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }




        locationsState.errorMessage?.let { message ->
            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun DataNotExist(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            modifier = modifier
                .size(150.dp)
                .padding(16.dp),
            imageVector = Icons.Rounded.Info,
            contentDescription = "Image data not exist",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Belum ada lokasi tersimpan.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Text(
            text = "Tambahkan lokasi dahulu!",
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier.padding(
                8.dp
            )
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationList(
    modifier: Modifier = Modifier,
    locations: List<Location>?,
    navHostController: NavHostController?,
    onDeleteLocation: (Location) -> Unit,
    onAddImage: (Location) -> Unit
) {

    val context = LocalContext.current
    locations?.let {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp),
        ) {
            items(it, key = {
                it.id
            }) { location ->

                LocationItem(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing,
                        )
                    ),
                    location = location,
                    onDirectionClick = {
                        ImplicitIntent.toMapsDirection(
                            context,
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                    },
                    onShareClick = {
                        ImplicitIntent.shareLocation(context, it.name, it.latitude, it.longitude)
                    },
                    onDeleteLocation = {
                        onDeleteLocation(it)
                    },
                    onEditNote = {
                        navHostController?.navigate(Route.EditNote.createRoute(it.id.toString()))
                    },
                    onEditTitle = {
                        navHostController?.navigate(Route.EditTitle.createRoute(it.id.toString()))
                    },
                    onAddImage = {
                        onAddImage(it)
                    })
            }

        }

    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        locationsState = LocationState(),
        navController = null,
        onDeleteLocation = {},
        onAddImage = {}
    )
}