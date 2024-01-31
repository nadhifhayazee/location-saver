package com.nadhifhayazee.domain.use_case.location

import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    suspend operator fun invoke(id: String): Flow<ResultState<Unit>> {

        return flow {
            try {
                emit(ResultState.Loading())
                delay(300)
                locationRepository.deleteLocation(id)
                emit(ResultState.Success(Unit))
            } catch (e: Exception) {
                emit(ResultState.Error(e))
            }
        }

    }
}