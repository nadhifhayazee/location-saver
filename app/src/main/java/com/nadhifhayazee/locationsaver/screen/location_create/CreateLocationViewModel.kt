package com.nadhifhayazee.locationsaver.screen.location_create

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.useCase.location.AddLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val addLocationUseCase: AddLocationUseCase
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationState?>(null)
    val locationState get() = _locationState.asStateFlow()

    private val _locationAddress = MutableStateFlow("-")
    val locationAddress get() = _locationAddress.asStateFlow()

    private val _isGPSActive = MutableStateFlow(false)
    val isGPSActive get() = _isGPSActive.asStateFlow()

    private val _addResult = MutableSharedFlow<ResultState<Boolean>>()
    val addResult get() = _addResult.asSharedFlow()

    private var locationRequest: LocationRequest
    private var locationSettingsBuilder: LocationSettingsRequest.Builder
    private var locationSettingClient: SettingsClient

    init {
        _isGPSActive.value = checkIsGPSActive()
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            20000
        ).build()
        locationSettingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            locationRequest
        )
        locationSettingClient = LocationServices.getSettingsClient(context)
    }

    fun updateLocationName(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude, longitude, 1
            ) {
                if (it.isNotEmpty()) {
                    _locationAddress.value = it[0].getAddressLine(0)
                }
            }
        } else {
            try {
                val list = geocoder.getFromLocation(latitude, longitude, 1)
                if (list?.isNotEmpty() == true) {
                    _locationAddress.value = list[0].getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun checkIsGPSActive(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestEnableGPS() {
        val task: Task<LocationSettingsResponse> =
            locationSettingClient.checkLocationSettings(locationSettingsBuilder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    _locationState.value = LocationState.GPSNotActive(intentSenderRequest)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {

            if (!checkIsGPSActive()) {
                requestEnableGPS()
                return@launch
            }
            try {
                val fusedLocation =
                    LocationServices.getFusedLocationProviderClient(context)

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
                    ).addOnFailureListener {
                        Log.d("getCurrentLocation", "getCurrentLocation: ${it.message}")
                    }

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
                _addResult.emit(it)
            }
        }
    }

}

sealed class LocationState() {
    data class GPSNotActive(val intentSenderRequest: IntentSenderRequest) : LocationState()
    data class CurrentLocation(val location: Location?) : LocationState()
}