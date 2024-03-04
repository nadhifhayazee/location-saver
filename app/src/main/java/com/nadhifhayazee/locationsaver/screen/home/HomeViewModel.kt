package com.nadhifhayazee.locationsaver.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.useCase.location.DeleteLocationUseCase
import com.nadhifhayazee.domain.useCase.location.GetMyLocationsUseCase
import com.nadhifhayazee.domain.useCase.locationImage.AddLocationImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyLocationsUseCase: GetMyLocationsUseCase,
    private val addLocationImageUseCase: AddLocationImageUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,

) : ViewModel() {

    private val _myLocations = MutableStateFlow<LocationState>(LocationState())
    val myLocations get() = _myLocations.asStateFlow()


    init {
        getMyLocations()
    }

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.DeleteLocation -> {
                deleteLocation(event.id)
            }
        }
    }

    private fun getMyLocations() {
        viewModelScope.launch {
            getMyLocationsUseCase()
                .collectLatest { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            _myLocations.update { it.copy(loading = true, errorMessage = null) }
                        }
                        is ResultState.Success -> {
                            _myLocations.update { it.copy(loading = false, locations = result.data, errorMessage = null) }
                        }
                        is ResultState.Error -> {
                            _myLocations.update { it.copy(loading = false, errorMessage = result.throwable.message) }
                        }
                        else -> Unit
                    }
                }
        }
    }
    private fun deleteLocation(id: String) {
        viewModelScope.launch {
            deleteLocationUseCase(id).collectLatest {  }
        }
    }

    fun addLocationImage(imageName: String, locationId: Int) {
        viewModelScope.launch {
            addLocationImageUseCase(imageName, locationId).collectLatest {

            }
        }
    }

}