package com.nadhifhayazee.locationsaver.screen.location_create

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.nadhifhayazee.domain.useCase.location.AddLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateLocationViewModel @Inject constructor(
    private val addLocationUseCase: AddLocationUseCase
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationState?>(null)
    val locationState get() = _locationState.asStateFlow()



    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun checkLocationPermission(context: Context) {
        viewModelScope.launch {
            if (permissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                _locationState.value = LocationState.Permission(true)
            } else {
                _locationState.value = LocationState.Permission(false)
            }
        }
    }

    fun checkGPSState(context: Context) {
        viewModelScope.launch {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            _locationState.value = LocationState.GPSState(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }

    fun getCurrentLocation(context: Context) {
        viewModelScope.launch {

            try {
                val fusedLocation =
                    LocationServices.getFusedLocationProviderClient(context)
                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = 20 * 1000
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation.let {
                            fusedLocation.removeLocationUpdates(this)
                            _locationState.value = LocationState.CurrentLocation(it)
                        }
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocation.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun addLocation(
        name: String,
        detail: String?,
        latitude: Double,
        longitude: Double
    ) {
        val location = com.nadhifhayazee.domain.model.Location(
            name = name,
            locationDetail = detail,
            longitude = longitude,
            latitude = latitude
        )

        viewModelScope.launch {
            addLocationUseCase(location).collectLatest {

            }
        }
    }

}

sealed class LocationState() {
    data class Permission(val isGranted: Boolean?) : LocationState()
    data class GPSState(val isActive: Boolean?) : LocationState()
    data class CurrentLocation(val location: Location?) : LocationState()
}