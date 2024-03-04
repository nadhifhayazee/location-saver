package com.nadhifhayazee.locationsaver.screen.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.locationsaver.navigation.HomeNavigation
import com.nadhifhayazee.locationsaver.screen.home.component.LocationItem
import com.nadhifhayazee.shared.tools.ImageUriGenerator

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeNavigation: HomeNavigation?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val locationsState by homeViewModel.myLocations.collectAsState()
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current
    var uri: Uri? = null
    var selectedLocation by remember {
        mutableStateOf<Location?>(null)
    }
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

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            uri = ImageUriGenerator.createImageUri(context)
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Log.d("LocationScreen", "HomeScreen")

    Scaffold(
        floatingActionButton = {
            AddLocationFloatingButton(
                homeNavigation = homeNavigation
            )
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->


        HomeScreenContent(
            modifier = modifier,
            locationsState = locationsState,
            homeNavigation,
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
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        )
    }


}


@Composable
fun AddLocationFloatingButton(
    modifier: Modifier = Modifier,
    homeNavigation: HomeNavigation?
) {

    Button(
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.White),
        onClick = {
            homeNavigation?.actionHomeScreenToCreateLocationScreen()

        }) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = modifier
                    .size(24.dp)
                    .padding(horizontal = 4.dp),
                imageVector = Icons.Sharp.LocationOn,
                contentDescription = "Icon add location",
                tint = Color(0XFFFFA500)
            )

            Text(
                text = "Tambahkan",
                color = Color(0XFFFFA500),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    locationsState: LocationState,
    homeNavigation: HomeNavigation?,
    paddingValues: PaddingValues = PaddingValues(),
    onDeleteLocation: (Location) -> Unit,
    onAddImage: (Location) -> Unit
) {

    Log.d("LocationScreen", "HomeScreenContent")

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                color = Color.LightGray.copy(alpha = 0.2f)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AnimatedVisibility(visible = locationsState.loading) {
            CircularProgressIndicator()
        }


        if (!locationsState.loading && locationsState.locations.isEmpty()) {
            DataNotExist()
        } else {
            LocationList(
                locations = locationsState.locations,
                homeNavigation = homeNavigation,
                onDeleteLocation = { location ->
                    onDeleteLocation(location)
                },
                onAddImage = { location ->
                    onAddImage(location)
                }
            )
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
                .size(100.dp)
                .padding(16.dp),
            imageVector = Icons.Default.List,
            contentDescription = "Image data not exist",
            tint = Color.LightGray
        )

        Text(text = "Belum ada data!", color = Color.Gray)

    }
}

@Composable
fun LocationList(
    modifier: Modifier = Modifier,
    locations: List<Location>?,
    homeNavigation: HomeNavigation?,
    onDeleteLocation: (Location) -> Unit,
    onAddImage: (Location) -> Unit
) {

    Log.d("LocationItemList", "LocationList")

    val context = LocalContext.current
    locations?.let {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp),
        ) {
            items(it, key = {
                it.id
            }) { location ->

                val locationItem by remember {
                    mutableStateOf(location)
                }
                LocationItem(modifier = Modifier.padding(vertical = 8.dp),
                    location = locationItem,
                    onDirectionClick = {
                        homeNavigation?.gotoToDirection(
                            context,
                            it.latitude.toString(),
                            it.longitude.toString()
                        )
                    },
                    onShareClick = {
                        shareLocation(context, it.name, it.latitude, it.longitude)
                    },
                    onDeleteLocation = {
                        onDeleteLocation(it)
                    },
                    onEditNote = {
                        homeNavigation?.navigateToEditNote(it.id.toString())
                    },
                    onEditTitle = {
                        homeNavigation?.navigateToEditTitle(it.id.toString())
                    },
                    onAddImage = {
                        onAddImage(it)
                    })
            }

        }

    }

}


private fun shareLocation(context: Context, name: String?, lat: Double?, lng: Double?) {

    val header = "Berbagi lokasi $name \n"
    val message = "$header https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_SUBJECT,
            header
        )
        putExtra(
            Intent.EXTRA_TEXT,
            message
        )
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            message
        )
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        locationsState = LocationState(),
        homeNavigation = null,
        onDeleteLocation = {},
        onAddImage = {}
    )
}