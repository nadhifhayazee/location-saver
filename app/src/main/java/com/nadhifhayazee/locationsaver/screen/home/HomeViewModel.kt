package com.nadhifhayazee.locationsaver.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.use_case.location.AddLocationUseCase
import com.nadhifhayazee.domain.use_case.location.DeleteLocationUseCase
import com.nadhifhayazee.domain.use_case.location.GetMyLocationsUseCase
import com.nadhifhayazee.domain.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyLocationsUseCase: GetMyLocationsUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase
) : ViewModel() {

    private val _myLocations = MutableStateFlow<MyLocationsState>(initialMyLocationsState())
    val myLocations get() = _myLocations.asStateFlow()

    private val _addLocationState = MutableSharedFlow<ResultState<Boolean>>()
    val addLocationState get() = _addLocationState.asSharedFlow()


    private fun initialMyLocationsState() = MyLocationsState(
        isLoading = true,
        locations = null,
        throwable = null
    )

    init {
        getMyLocations()
    }

    fun getMyLocations() {
        viewModelScope.launch {
            getMyLocationsUseCase()
                .collectLatest { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            _myLocations.update { it.copy(isLoading = true, throwable = null) }
                        }
                        is ResultState.Success -> {
                            _myLocations.update { it.copy(isLoading = false, locations = result.data, throwable = null) }
                        }
                        is ResultState.Error -> {
                            _myLocations.update { it.copy(isLoading = false, throwable = result.throwable) }
                        }
                        else -> Unit
                    }
                }
        }
    }

    fun addLocation(
        name: String,
        detail: String?,
        longitude: Double,
        latitude: Double
    ) {
        val location = Location(
            name = name,
            locationDetail = detail,
            longitude = longitude,
            latitude = latitude
        )

        viewModelScope.launch {
            addLocationUseCase(location).collectLatest {
                _addLocationState.emit(it)
            }
        }
    }

    fun deleteLocation(id: String) {
        viewModelScope.launch {
            deleteLocationUseCase(id).collectLatest {  }
        }
    }

}