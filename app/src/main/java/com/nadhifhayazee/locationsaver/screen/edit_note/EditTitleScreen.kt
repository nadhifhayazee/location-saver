package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.locationsaver.component.ClearAbleTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditTitleScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    locationId: Int?,
    navController: NavHostController?
) {

    val location by viewModel.location.collectAsState()
    var locationTitle by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(true) {
        locationId?.let { viewModel.getLocationDetail(it) }
        viewModel.editResult.collectLatest { editResult ->
            when (editResult) {
                is ResultState.Success -> {
                    if (editResult.data) {
                        navController?.popBackStack()
                    }
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            EditLocationTopBar(
                title = "Edit Nama Lokasi",
                isButtonEnabled = locationTitle?.isNotBlank() == true,
                onCancelClicked = {
                    navController?.popBackStack()
                },
                onSaveClicked = {
                    viewModel.updateTitle(locationTitle!!)
                }
            )

        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            ClearAbleTextField(
                value = location?.name ?: "",
                placeholder = "Edit Nama Lokasi"
            ) {
                locationTitle = it
            }
        }
    }
}

