package com.nadhifhayazee.locationsaver.screen.location_create

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.locationsaver.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(MapsComposeExperimentalApi::class)
@SuppressLint("MissingPermission")
@Composable
fun CreateLocationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    createLocationViewModel: CreateLocationViewModel = hiltViewModel()

) {
    val coroutineScope = rememberCoroutineScope()
    val defaultLatLng = LatLng(-6.200000, 106.816666)
    val uiState by createLocationViewModel.uiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        this.position =
            CameraPosition.fromLatLngZoom(uiState.currentLocation ?: defaultLatLng, 12f)
    }
    val markerState = rememberUpdatedMarkerState(
        position = cameraPositionState.position.target
    )

    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                createLocationViewModel.getCurrentLocation()
            }

        }





    LaunchedEffect(true) {
        if (createLocationViewModel.checkIsGPSActive()) {
            createLocationViewModel.getCurrentLocation()
        } else {
            createLocationViewModel.requestEnableGPS {
                locationRequestLauncher.launch(it)
            }
        }

        createLocationViewModel.addResult.collectLatest { result ->
            when (result) {
                is ResultState.Success -> {
                    if (result.data) {
                        navController?.popBackStack()
                    }
                }

                else -> Unit
            }
        }
    }


    LaunchedEffect(uiState.currentLocation) {
        if (uiState.currentLocation != null) {
            animateCameraToCurrentLocation(
                cameraPositionState,
                uiState.currentLocation!!
            )
        }
    }


    Scaffold(

        topBar = {
            TopBar(
                isLoading = uiState.isLoading,
                locationName = uiState.selectedLocationAddress ?: "",
                onAddLocationClicked = {
                    val selectedLatLng = cameraPositionState.position.target
                    createLocationViewModel.addLocation(
                        name = uiState.selectedLocationAddress ?: "",
                        latitude = selectedLatLng.latitude,
                        longitude = selectedLatLng.longitude,
                        detail = null
                    )
                }) {
                navController?.popBackStack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            GoogleMap(
                modifier = modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = false,
                    zoomControlsEnabled = false
                ),
            ) {

                if (uiState.currentLocation != null) {
                    Marker(
                        state = markerState
                    )
                }


                MapEffect { map ->
                    map.setOnCameraIdleListener {
                        if (uiState.currentLocation != null) {
                            val latLng = cameraPositionState.position.target
                            createLocationViewModel.updateLocationName(
                                latLng.latitude,
                                latLng.longitude
                            )
                        }
                    }
                }

            }

            CurrentLocationButton(
                modifier = modifier
                    .padding(16.dp)
                    .align(
                        Alignment.BottomEnd
                    )
            ) {
                coroutineScope.launch {
                    if (uiState.currentLocation != null) {
                        animateCameraToCurrentLocation(
                            cameraPositionState,
                            uiState.currentLocation!!
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    locationName: String,
    onAddLocationClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
                onBackClicked()

            }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pilih lokasi",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(20.dp)
                )
            } else {
                Text(
                    text = locationName, color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }


        }



        TextButton(
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {
                onAddLocationClicked()
            },
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text(

                text = "Oke",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }


    }
}


@Composable
fun CurrentLocationButton(modifier: Modifier = Modifier, onClicked: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = { onClicked() },
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Icon(
            modifier = Modifier.size(21.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_current_location),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Current location button"
        )
    }
}


suspend fun animateCameraToCurrentLocation(
    cameraPositionState: CameraPositionState,
    currentLocationLatLng: LatLng
) {
    cameraPositionState.animate(
        update = CameraUpdateFactory.newCameraPosition(
            CameraPosition.fromLatLngZoom(currentLocationLatLng, 18f)
        ), durationMs = 700
    )
}

