package com.nadhifhayazee.domain.useCase.location

import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    operator fun invoke(location: Location): Flow<ResultState<Boolean>>{
        return flow {
            emit(ResultState.Loading())
            delay(300)
            repository.updateLocation(location)
            emit(ResultState.Success(true))
        }.catch {
            emit(ResultState.Error(it))
        }
    }
}