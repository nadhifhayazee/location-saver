package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.useCase.location.GetLocationDetailUseCase
import com.nadhifhayazee.domain.useCase.location.UpdateLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val getLocationDetailUseCase: GetLocationDetailUseCase
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location get() = _location.asStateFlow()

    fun getLocationDetail(locationId: Int) {
        viewModelScope.launch {
            getLocationDetailUseCase(locationId.toString()).collectLatest {
                _location.value = it
            }
        }
    }

    fun updateNote(newNote: String) {
        viewModelScope.launch {
            val location = _location.value?.copy(locationDetail = newNote)
            location?.let { updateLocationUseCase(it).collectLatest {  } }
        }
    }

    fun updateTitle(newTitle: String) {
        viewModelScope.launch {
            val location = _location.value?.copy(name = newTitle)
            location?.let { updateLocationUseCase(it).collectLatest {  } }
        }
    }

}