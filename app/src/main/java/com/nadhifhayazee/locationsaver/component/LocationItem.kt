package com.nadhifhayazee.locationsaver.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nadhifhayazee.domain.model.Location

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    location: Location,
    onDirectionClick: (Location) -> Unit,
    onShareClick: (Location) -> Unit,
    onDeleteLocation: (Location) -> Unit
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White)
    ) {


        Column(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = location.name ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )

            Spacer(modifier = modifier.padding(top = 4.dp))

            Text(
                text = location.locationDetail ?: ""
            )

            Spacer(modifier = modifier.padding(top = 4.dp))


            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row {

                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Blue.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        onClick = { onDirectionClick.invoke(location) }) {
                        Icon(
                            modifier = modifier
                                .size(18.dp)
                                .padding(end = 4.dp),
                            imageVector = Icons.Rounded.Send,
                            contentDescription = "Button show route",
                        )
                        Text(text = "Rute", fontSize = 12.sp)
                    }

                    Button(
                        modifier = modifier.padding(start = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Blue.copy(alpha = 0.8f),
                            backgroundColor = Color.White
                        ),
                        onClick = { onShareClick.invoke(location) }) {
                        Icon(
                            modifier = modifier
                                .size(18.dp)
                                .padding(end = 4.dp),
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Button share route",
                        )
                        Text(text = "Bagikan", fontSize = 12.sp)
                    }
                }

                IconButton(onClick = { onDeleteLocation(location) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Button",
                        tint = Color.Red,

                    )
                }
            }
        }


    }

}

@Preview
@Composable
fun LocationDetailPreview() {
    LocationItem(
        location = Location(0, "preview", "notes", null, null),
        onDirectionClick = {},
        onShareClick = {},
        onDeleteLocation = {}
    )
}