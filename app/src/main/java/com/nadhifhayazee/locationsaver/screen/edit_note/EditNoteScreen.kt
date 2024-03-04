package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nadhifhayazee.locationsaver.component.Theme

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = hiltViewModel(),
    locationId: Int?,
    navController: NavHostController?
) {

    val location by viewModel.location.collectAsState()


    LaunchedEffect(true) {
        locationId?.let { viewModel.getLocationDetail(it) }
    }



    var locationNote = remember {
        mutableStateOf(location?.locationDetail)
    }

    Scaffold(
        topBar = {
            EditLocationTopBar(
                title = if (location?.locationDetail == null) "Tambah Catatan" else "Edit Catatan",
                onCancelClicked = {
                    navController?.popBackStack()
                },
                onDoneClicked = {
                    locationNote.value?.let { viewModel.updateNote(it) }
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

            Text(
                text = location?.name ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp)
            )


            OutlinedTextField(
                value = locationNote.value ?: location?.locationDetail ?:"",
                onValueChange = { locationNote.value = it },
                placeholder = { Text(text = if (location?.locationDetail == null) "Tambah Catatan" else "Edit Catatan")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            )

        }
    }
}

@Composable
fun EditLocationTopBar(
    title: String,
    onCancelClicked: () -> Unit,
    onDoneClicked: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onCancelClicked() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "", tint = Color.Black)
            }


            Text(text = title, color = Color.Black)

            TextButton(onClick = { onDoneClicked() }) {
                Text(
                    text = "Selesai",
                    color = Theme.Color.blueBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }


    }
}

@Preview
@Composable
fun EditNoteTopBarPreview() {
    EditLocationTopBar(title = "Edit Catatan", onCancelClicked = { /*TODO*/ }) {

    }
}