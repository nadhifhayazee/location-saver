package com.nadhifhayazee.domain.useCase.locationImage

import com.nadhifhayazee.domain.model.LocationImage
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddLocationImageUseCase @Inject constructor(
    private val repository: LocationImagesRepository
) {

    operator fun invoke(imageName: String, locationId: Int): Flow<ResultState<Boolean>> {
        return flow {
            emit(ResultState.Loading())
            val t = repository.addLocationImage(LocationImage(name = imageName, locationId = locationId))
            emit(ResultState.Success(t))
        }.catch {
            emit(ResultState.Error(it))
        }
    }
}