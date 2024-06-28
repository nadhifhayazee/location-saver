package com.nadhifhayazee.locationsaver.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ClearAbleTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {

    var textFieldValue by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(value) {
        if (value.isNotEmpty() && textFieldValue.isNullOrEmpty()) {
            textFieldValue = value
        }
    }

    OutlinedTextField(
        trailingIcon = {
            if (!textFieldValue.isNullOrEmpty()) {
                IconButton(onClick = {
                    textFieldValue = ""
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = "Clear text field value",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                            .size(16.dp)
                    )
                }
            }

        },
        value = textFieldValue ?: value,
        onValueChange = {
            textFieldValue = it
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    )
}