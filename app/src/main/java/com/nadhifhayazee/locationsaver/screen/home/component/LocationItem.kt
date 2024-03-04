package com.nadhifhayazee.locationsaver.screen.home.component

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.locationsaver.R
import com.nadhifhayazee.locationsaver.component.Theme
import com.nadhifhayazee.shared.tools.getBitmap

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    location: Location,
    onDirectionClick: (Location) -> Unit,
    onShareClick: (Location) -> Unit,
    onDeleteLocation: (Location) -> Unit,
    onEditTitle: (Location) -> Unit,
    onEditNote: (Location) -> Unit,
    onAddImage: (Location) -> Unit,
) {


    Log.d("LocationItem", "LocationItem")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        TitleAndMenuSection(
            title = location.name,
            onEditTitle = {
                onEditTitle(location)
            },
            onAddImage = {
                onAddImage(location)
            },
            onDeleteLocation = {
                onDeleteLocation(location)
            }
        )


        DirectionAndShareButtonSection(
            onDirectionClick = { onDirectionClick(location) },
            onShareClick = { onShareClick(location) }
        )

        LocationImages(location.locationImages)

        LocationNote(
            note = location.locationDetail,
            onEditNoteClicked = {
                onEditNote(location)
            }
        )
    }

}

@Composable
fun TitleAndMenuSection(
    title: String?,
//    onMenuClicked: () -> Unit,
//    onMenuDismiss: () -> Unit,
    onEditTitle: () -> Unit,
    onAddImage: () -> Unit,
    onDeleteLocation: () -> Unit
) {

    var menuExpanded by remember {
        mutableStateOf(false)
    }


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
        )


        Column {

            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dot_option),
                    contentDescription = "",
                    modifier = Modifier.size(18.dp)
                )
            }


            LocationDropdownMenu(expanded = menuExpanded,
                onDismiss = { menuExpanded = false },
                onEditTitle = {
                    menuExpanded = false
                    onEditTitle()
                },
                onAddImage = {
                    menuExpanded = false
                    onAddImage()
                },
                onDelete = {
                    menuExpanded = false
                    onDeleteLocation()
                })


        }
    }


}

@Composable
fun LocationDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditTitle: () -> Unit,
    onAddImage: () -> Unit,
    onDelete: () -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = { onDismiss() }) {
        Column {
            LocationItemSetting(name = "Edit nama lokasi") {
                onEditTitle()
            }

            LocationItemSetting(name = "Tambah foto lokasi") {
                onAddImage()
            }

            LocationItemSetting(name = "Hapus lokasi") {
                onDelete()
            }
        }
    }
}

@Composable
fun LocationItemSetting(
    modifier: Modifier = Modifier, name: String, onClick: () -> Unit
) {

    TextButton(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = { onClick() }) {
        Text(
            text = name, color = Color.Black
        )
    }
}


@Composable
fun DirectionAndShareButtonSection(
    onDirectionClick: () -> Unit,
    onShareClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Theme.Color.blueBackground, contentColor = Color.White
            ),
            onClick = { onDirectionClick.invoke() }) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp),
                painter = painterResource(id = R.drawable.ic_direction),
                contentDescription = "Button show route",
            )
            Text(text = "Rute", fontSize = 12.sp)
        }

        Button(modifier = Modifier.padding(start = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF008000),
                backgroundColor = Color.White,

                ),
            border = BorderStroke(1.dp, Color(0xFF008000)),
            onClick = { onShareClick.invoke() }) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp),
                imageVector = Icons.Rounded.Share,
                contentDescription = "Button share route",
            )
            Text(text = "Bagikan", fontSize = 12.sp)
        }

    }
}


@Composable
fun LocationImages(
    locationImages: List<String>,

    ) {

    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(), content = {
        items(locationImages) { image ->


            ImageLocation(uri = image)

        }
    })

}

@Composable
fun ImageLocation(
    uri: String,
) {

    val content = LocalContext.current
    val imageBitmap by remember {
        mutableStateOf(Uri.parse(uri).getBitmap(content))
    }

    imageBitmap?.let {


        Card(
            modifier = Modifier
                .size(150.dp)
                .wrapContentHeight()
                .padding(end = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
            elevation = 0.dp
        ) {

            Box(


            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(160 / 120f)
                        .background(
                            color = Color.Transparent, shape = RoundedCornerShape(16.dp)
                        ),
                    model = imageBitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }


        }

    }
}

@Composable
fun LocationNote(
    modifier: Modifier = Modifier, note: String?, onEditNoteClicked: () -> Unit
) {

    Row(
        modifier = modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFF1ECEC), shape = RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = note ?: "Tambah catatan...",
            color = Color(0XFF242424),
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        IconButton(onClick = { onEditNoteClicked() }) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                tint = Color.Black,
                contentDescription = "",
                modifier = modifier
                    .padding(top = 4.dp, bottom = 8.dp, end = 16.dp)
                    .size(16.dp)
            )
        }

    }

}

@Preview
@Composable
fun LocationDetailPreview() {
    LocationItem(location = Location(0, "preview", "notes", null, null, listOf("", "")),
        onDirectionClick = {},
        onShareClick = {},
        onDeleteLocation = {},
        onAddImage = {},
        onEditNote = {},
        onEditTitle = {})
}