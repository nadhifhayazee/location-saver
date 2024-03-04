package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun EditTitleScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    locationId: Int?,
    navController: NavHostController?
) {

    val location by viewModel.location.collectAsState()


    LaunchedEffect(true) {
        locationId?.let { viewModel.getLocationDetail(it) }
    }


    var locationTitle = remember {
        mutableStateOf(location?.name)
    }

    Scaffold(
        topBar = {
            EditLocationTopBar(
                title = "Edit Nama Lokasi",
                onCancelClicked = {
                    navController?.popBackStack()
                },
                onDoneClicked = {
                    locationTitle.value?.let { viewModel.updateTitle(it) }
                    navController?.popBackStack()
                }
            )

        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            OutlinedTextField(
                value = locationTitle.value ?: location?.name ?: "",
                onValueChange = { locationTitle.value = it },
                placeholder = { Text(text = "Edit Nama Lokasi") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)

            )

        }
    }
}

