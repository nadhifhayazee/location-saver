package com.nadhifhayazee.locationsaver.screen.location_create

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.nadhifhayazee.locationsaver.screen.location_create.component.LocationPermissionState


@SuppressLint("MissingPermission")
@Composable
fun CreateLocationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    createLocationViewModel: CreateLocationViewModel = hiltViewModel()

) {
    val context = LocalContext.current

    val locationState by createLocationViewModel.locationState.collectAsState()

    if (locationState == null) {
        createLocationViewModel.checkLocationPermission(context)
    }

    when (locationState) {
        is LocationState.Permission -> {
            if ((locationState as LocationState.Permission).isGranted == true) {
                createLocationViewModel.checkGPSState(context)
            } else {

                LocationPermissionState(onPermissionAllowed = {
                    createLocationViewModel.checkGPSState(context)
                }) {
                    Toast.makeText(
                        context, "Location Permission must be allowed!", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        is LocationState.GPSState -> {
            if ((locationState as LocationState.GPSState).isActive == true) {
                createLocationViewModel.getCurrentLocation(context)
            } else {
                AlertDialog(
                    onDismissRequest = { createLocationViewModel.checkGPSState(context) },
                    buttons = {

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        ) {
                            Button(onClick = {
                                createLocationViewModel.checkGPSState(context)
                            }) {
                                Text(text = "Oke")
                            }
                        }
                    },
                    title = {
                        Text(text = "Activate your GPS please!")
                    },
                    text = {
                        Text(text = "This app need to activate GPS")
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
            }
        }

        is LocationState.CurrentLocation -> {

            CreateLocationContent(
                location = (locationState as LocationState.CurrentLocation).location,
                onAddLocationClicked = { name, detail, lat, lng ->
                    createLocationViewModel.addLocation(name, detail, lat, lng)
                    navController?.popBackStack()
                })
        }

        else -> {}
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateLocationContent(
    modifier: Modifier = Modifier,
    location: Location?,
    onAddLocationClicked: (String, String?, Double, Double) -> Unit
) {
    val marker = rememberMarkerState()
    val latLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
    marker.position = latLng

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 16f)
    }

    Scaffold { paddingValue ->


        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {


            GoogleMap(
                modifier = modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(myLocationButtonEnabled = true)

            ) {

                Marker(
                    state = MarkerState(position = latLng),
                    icon = BitmapDescriptorFactory.defaultMarker()
                )

                MapEffect(latLng) {

                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(color = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .align(Alignment.TopCenter)
                        .background(color = Color.White),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(

                            onClick = {

                            }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "",
                                tint = Color.Black,
                            )
                        }

                        Text(
                            text = "Pilih lokasi",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }


                    TextButton(
                        onClick = {
                            onAddLocationClicked(
                                "${latLng.latitude}, ${latLng.longitude}",
                                null,
                                latLng.latitude,
                                latLng.longitude
                            )
                        }
                    ) {
                        Text(
                            text = "Oke",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }


                }


            }

//            BottomSheetScaffold(
//                sheetContent = {
//                    AddLocationSection(
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .background(shape = RoundedCornerShape(12.dp), color = Color.White),
//                        onAddLocationClicked = { name, detail ->
//                            onAddLocationClicked(name, detail, latLng.latitude, latLng.longitude)
//                        }
//                    )
//                }, sheetShape = RoundedCornerShape(16.dp),
//                scaffoldState = bottomSheetScaffoldState
//            ) {
//
//            }

        }
    }


}

@Composable
fun AddLocationSection(
    modifier: Modifier, onAddLocationClicked: (String, String) -> Unit
) {

    var locationName = remember {
        mutableStateOf("")
    }
    var locationDetail = remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
    ) {


        OutlinedTextField(modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
            value = locationName.value,
            label = { Text(text = "Nama Lokasi") },
            onValueChange = {
                locationName.value = it
            })

        OutlinedTextField(modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            value = locationDetail.value,
            label = { Text(text = "Detail Lokasi") },
            onValueChange = {
                locationDetail.value = it
            })



        TextButton(modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                onAddLocationClicked(
                    locationName.value, locationDetail.value
                )
            }) {
            Text(text = "Tambah")
        }
    }
}


@Preview
@Composable
fun CreateLocationScreenPreview() {

    CreateLocationContent(location = null, onAddLocationClicked = {_,_,_,_ -> })
}