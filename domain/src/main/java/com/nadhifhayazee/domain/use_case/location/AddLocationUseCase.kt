package com.nadhifhayazee.domain.use_case.location

import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(location: Location): Flow<ResultState<Boolean>> {
        return flow {
            emit(ResultState.Loading())
            delay(300)
            val addLocation = locationRepository.addLocation(location)
            emit(ResultState.Success(addLocation))
        }.catch {
            emit(ResultState.Error(it))
        }
    }
}