package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.locationsaver.component.ClearAbleTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    locationId: Int?,
    navController: NavHostController?
) {

    val location by viewModel.location.collectAsState()

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

    val locationNote = remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EditLocationTopBar(
                title = if (location?.locationDetail == null) "Tambah Catatan" else "Edit Catatan",
                onCancelClicked = {
                    navController?.popBackStack()
                },
                onSaveClicked = {
                    viewModel.updateNote(locationNote.value ?: "")
                }
            )

        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {

            Text(
                text = location?.name ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp)
            )


            ClearAbleTextField(
                value = location?.locationDetail ?: "",
                placeholder = if (location?.locationDetail == null) "Tambah Catatan" else "Edit Catatan"
            ) {
                locationNote.value = it
            }
//            OutlinedTextField(
//                value = locationNote.value ?: location?.locationDetail ?: "",
//                onValueChange = { locationNote.value = it },
//                placeholder = {
//                    Text(
//                        text = if (location?.locationDetail == null) "Tambah Catatan" else "Edit Catatan",
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium)
//                    )
//                },
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
//                    focusedContainerColor = MaterialTheme.colorScheme.surface
//                ),
//
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//
//            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLocationTopBar(
    title: String,
    isButtonEnabled: Boolean = true,
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { onCancelClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }


                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                TextButton(
                    enabled = isButtonEnabled,
                    onClick = { onSaveClicked() },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Simpan",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

            }
        }
    )
}

@Preview
@Composable
fun EditNoteTopBarPreview() {
    EditLocationTopBar(
        title = "Edit Catatan",
        isButtonEnabled = true,
        onCancelClicked = { /*TODO*/ }) {

    }
}