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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.locationsaver.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun CreateLocationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    createLocationViewModel: CreateLocationViewModel = hiltViewModel()

) {
    val coroutineScope = rememberCoroutineScope()
    val locationState by createLocationViewModel.locationState.collectAsState()
    val selectedLocationName by createLocationViewModel.locationAddress.collectAsState()
    val isGPSActive by createLocationViewModel.isGPSActive.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val defaultLatLng = LatLng(-6.200000, 106.816666)
    val cameraPositionState = rememberCameraPositionState {
        this.position =
            CameraPosition.fromLatLngZoom(currentLocation ?: defaultLatLng, 12f)
    }
    val markerState = rememberMarkerState()

    val locationRequestLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                createLocationViewModel.getCurrentLocation()
            }

        }

    LaunchedEffect(true) {
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

    LaunchedEffect(isGPSActive) {
        if (isGPSActive) {
            if (locationState == null) createLocationViewModel.getCurrentLocation()
        } else {
            createLocationViewModel.requestEnableGPS()
        }
    }

    LaunchedEffect(cameraPositionState.position.target) {
        markerState.position = cameraPositionState.position.target
    }

    LaunchedEffect(currentLocation) {
        if (currentLocation != null) {
            isLoading = false
            animateCameraToCurrentLocation(
                cameraPositionState,
                currentLocation!!
            )
        }
    }

    LaunchedEffect(locationState) {
        when (locationState) {
            is LocationState.GPSNotActive -> {
                locationRequestLauncher.launch((locationState as LocationState.GPSNotActive).intentSenderRequest)
            }

            is LocationState.CurrentLocation -> {
                val newLat = (locationState as LocationState.CurrentLocation).location?.latitude
                val newLng = (locationState as LocationState.CurrentLocation).location?.longitude
                if (currentLocation?.latitude != newLat || currentLocation?.longitude != newLng) {
                    currentLocation = LatLng(
                        newLat ?: 0.0,
                        newLng ?: 0.0
                    )
                }
            }

            else -> {}
        }
    }

    Scaffold(

        topBar = {
            TopBar(
                isEnabled = !isLoading,
                locationName = selectedLocationName,
                onAddLocationClicked = {
                    val selectedLatLng = cameraPositionState.position.target
                    createLocationViewModel.addLocation(
                        name = selectedLocationName,
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

                if (currentLocation != null) {
                    Marker(
                        state = markerState
                    )
                }


                MapEffect { map ->
                    map.setOnCameraIdleListener {
                        if (currentLocation != null) {
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
                    if (currentLocation != null) {
                        animateCameraToCurrentLocation(
                            cameraPositionState,
                            currentLocation!!
                        )
                    } else {
                        createLocationViewModel.requestEnableGPS()
                    }
                }
            }
        }
    }

}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
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



        TextButton(
            enabled = isEnabled,
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

