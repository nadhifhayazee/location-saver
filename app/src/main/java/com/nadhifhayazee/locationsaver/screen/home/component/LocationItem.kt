package com.nadhifhayazee.locationsaver.screen.home.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.locationsaver.R

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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.large
                )
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
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

            if (location.locationUriImages.isNotEmpty()) LocationImages(location.locationUriImages)
            else Spacer(modifier = modifier.height(4.dp))

            LocationNote(
                note = location.locationDetail,
                onEditNoteClicked = {
                    onEditNote(location)
                }
            )
        }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp)
        )


        Column {


            IconButton(onClick = { menuExpanded = true }, modifier = Modifier.size(28.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dot_option),
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp)
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
    DropdownMenu(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface),
        expanded = expanded,
        onDismissRequest = { onDismiss() }) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        onClick = { onClick() }) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.inverseSurface,
            textAlign = TextAlign.Start,
            modifier = modifier.fillMaxWidth()
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
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { onDirectionClick.invoke() }) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(id = R.drawable.ic_direction),
                contentDescription = "Button show route",
            )
            Text(
                text = "Rute",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Button(modifier = Modifier.padding(start = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            onClick = { onShareClick.invoke() }) {
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp),
                imageVector = Icons.Rounded.Share,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = "Button share route",
            )
            Text(
                text = "Bagikan",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
fun LocationImages(
    locationImages: List<Uri>,

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
    uri: Uri,
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .size(150.dp)
            .wrapContentHeight()
            .padding(end = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {

        Box {
            AsyncImage(
                modifier = Modifier
                    .clickable {
                        openImageViewer(context, uri)
                    }
                    .fillMaxWidth()
                    .aspectRatio(160 / 120f)
                    .background(
                        color = Color.Transparent, shape = RoundedCornerShape(16.dp)
                    ),
                model = uri,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }


    }
}


@Composable
fun LocationNote(
    modifier: Modifier = Modifier, note: String?, onEditNoteClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onEditNoteClicked()
                }
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = note ?: "Tambah catatan...",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            Icon(
                imageVector = Icons.Rounded.Edit,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "",
                modifier = modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
                    .size(16.dp)
            )

        }
    }

}


fun openImageViewer(context: Context, uri: Uri) {
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        setDataAndType(uri, "image/*")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(intent)
}

@Preview
@Composable
fun LocationDetailPreview() {
    LocationItem(location = Location(
        0,
        "-232321099421",
        "notes",
        null,
        null,
        listOf("", "")
    ),
        onDirectionClick = {},
        onShareClick = {},
        onDeleteLocation = {},
        onAddImage = {},
        onEditNote = {},
        onEditTitle = {})
}