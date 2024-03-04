package com.nadhifhayazee.domain.useCase.location

import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationDetailUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(id: String): Flow<Location?> {
        return flow {

            val data = locationRepository.getLocationById(id)
            emit(data)

        }.catch {
//            emit()
        }
    }
}