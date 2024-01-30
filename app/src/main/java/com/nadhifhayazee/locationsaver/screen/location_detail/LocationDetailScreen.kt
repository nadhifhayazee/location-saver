package com.nadhifhayazee.locationsaver.screen.location_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nadhifhayazee.locationsaver.navigation.LocationDetailNavigation

@Composable
fun LocationDetailScreen(
    modifier: Modifier = Modifier,
    locationDetailNavigation: LocationDetailNavigation?,
    locationId: String?
) {

    Column (modifier = modifier.fillMaxSize()){
        Text(text = "detail:$locationId")
    }
}

@Preview
@Composable
fun LocationDetailScreenPreview() {

    LocationDetailScreen(locationDetailNavigation = null, locationId = null)
}