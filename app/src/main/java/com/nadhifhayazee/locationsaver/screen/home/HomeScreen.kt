package com.nadhifhayazee.locationsaver.screen.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nadhifhayazee.locationsaver.component.LocationItem
import com.nadhifhayazee.locationsaver.navigation.HomeNavigation
import com.nadhifhayazee.shared.model.MyLocation

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeNavigation: HomeNavigation?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val locationsState by homeViewModel.myLocations.collectAsState()

    HomeScreenContent(modifier = modifier, locationsState = locationsState, homeNavigation) {
        homeViewModel.deleteLocation(it.id.toString())
    }

}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    locationsState: MyLocationsState,
    homeNavigation: HomeNavigation?,
    onDeleteLocation: (MyLocation) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            AddLocationFloatingButton(
                homeNavigation = homeNavigation
            )
        }
    ) { paddingValues ->

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


            AnimatedVisibility(visible = locationsState.isLoading) {
                CircularProgressIndicator()
            }


            if (!locationsState.isLoading && locationsState.locations.isNullOrEmpty()) {
                DataNotExist()
            } else {
                LocationList(
                    locations = locationsState.locations,
                    homeNavigation = homeNavigation
                ) { location ->
                    onDeleteLocation(location)
                }
            }


            locationsState.throwable?.let {
                Toast.makeText(LocalContext.current, it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
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
                tint = Color.Blue.copy(alpha = 0.8f)
            )

            Text(text = "Tambahkan", color = Color.Blue, fontSize = 11.sp)
        }

    }
}

@Composable
fun LocationList(
    modifier: Modifier = Modifier,
    locations: List<MyLocation>?,
    homeNavigation: HomeNavigation?,
    onDeleteLocation: (MyLocation) -> Unit
) {

    val context = LocalContext.current
    locations?.let {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(it) { location ->

                LocationItem(location = location, onDirectionClick = {
                    homeNavigation?.gotoToDirection(
                        context,
                        it.latitude.toString(),
                        it.longitude.toString()
                    )
                }, onShareClick = {
                    shareLocation(context, it.name, it.latitude, it.longitude)
                }, onDeleteLocation = {
                    onDeleteLocation(it)
                })
            }
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

private fun shareLocation(context: Context, name: String?, lat: Double?, lng: Double?) {

    val header = "Berbagi lokasi $name \n"
    val message = "$header" +
            "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"
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
        locationsState = MyLocationsState(isLoading = false, listOf(), null),
        homeNavigation = null
    ){}
}