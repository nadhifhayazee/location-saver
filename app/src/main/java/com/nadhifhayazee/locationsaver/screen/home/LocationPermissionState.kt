package com.nadhifhayazee.locationsaver.screen.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun LocationPermissionState(
    onPermissionAllowed: () -> Unit,
    onPermissionDenied: () -> Unit,
) {

    val launcherLocationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val areGranted = it.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            onPermissionAllowed()
        } else {
            onPermissionDenied()
        }
    }


    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val currentContext = LocalContext.current

    if (permissions.all {
            ContextCompat.checkSelfPermission(
                currentContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        onPermissionAllowed()
    } else {
        LaunchedEffect(true) {
            launcherLocationPermission.launch(permissions)
        }
    }


}

