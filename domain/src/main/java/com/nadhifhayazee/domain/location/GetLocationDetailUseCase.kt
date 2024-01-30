package com.nadhifhayazee.domain.location

import com.nadhifhayazee.shared.model.MyLocation
import com.nadhifhayazee.data.repository.location.LocationRepository
import com.nadhifhayazee.domain.model.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationDetailUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(id: String): Flow<ResultState<com.nadhifhayazee.shared.model.MyLocation>> {
        return flow {

            emit(ResultState.Loading())
            delay(300)
            val data = locationRepository.getLocationById(id)
            emit(ResultState.Success(data))

        }.catch {
            emit(ResultState.Error(it))
        }
    }
}