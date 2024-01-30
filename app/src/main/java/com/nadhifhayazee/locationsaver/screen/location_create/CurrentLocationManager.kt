package com.nadhifhayazee.locationsaver.screen.location_create

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

lateinit var fusedLocationClient: FusedLocationProviderClient
var locationCallback: LocationCallback? = null

@SuppressLint("MissingPermission")
@Composable
fun getCurrentLocation(context: Context): LatLng {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    var currentLocation by remember {
        mutableStateOf(LatLng(-6.1932982, 106.832034))
    }

    DisposableEffect(key1 = true) {

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.locations.last()

                currentLocation = LatLng(location.latitude, location.longitude)
                Log.d("GetLastLocation", "CreateLocationScreen: $currentLocation")
            }
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                currentLocation = LatLng(it.latitude, it.longitude)
            } else {
                updateLocation()

            }

        }.addOnFailureListener {
            Log.d("GetLastLocation", "CreateLocationScreen: ${it.message}")

        }

        onDispose {
            stopLocationUpdate()
        }
    }

    return currentLocation
}

fun stopLocationUpdate() {
    try {
        //Removes all location updates for the given callback.
        val removeTask = locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        removeTask?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("GetLastLocation", "Location Callback removed.")
            } else {
                Log.d("GetLastLocation", "Failed to remove Location Callback.")
            }
        }
    } catch (se: SecurityException) {
        Log.e("GetLastLocation", "Failed to remove Location Callback.. $se")
    }
}

@SuppressLint("MissingPermission")
fun updateLocation() {
    locationCallback?.let {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000
            ).build()
        //use FusedLocationProviderClient to request location update
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}