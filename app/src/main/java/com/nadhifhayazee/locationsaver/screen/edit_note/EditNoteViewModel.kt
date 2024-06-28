package com.nadhifhayazee.locationsaver.screen.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.useCase.location.GetLocationDetailUseCase
import com.nadhifhayazee.domain.useCase.location.UpdateLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _editResult = MutableSharedFlow<ResultState<Boolean>>()
    val editResult get() = _editResult.asSharedFlow()

    fun getLocationDetail(locationId: Int) {
        viewModelScope.launch {
            getLocationDetailUseCase(locationId.toString()).collectLatest {
                _location.value = it
            }
        }
    }

    fun updateNote(newNote: String) {
        viewModelScope.launch {
            val location = _location.value?.copy(locationDetail = newNote.ifEmpty { null })
            location?.let { it ->
                updateLocationUseCase(it).collectLatest { result ->
                    _editResult.emit(result)
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        viewModelScope.launch {
            val location = _location.value?.copy(name = newTitle)
            location?.let {
                updateLocationUseCase(it).collectLatest { result ->
                    _editResult.emit(
                        result
                    )
                }
            }
        }
    }

}