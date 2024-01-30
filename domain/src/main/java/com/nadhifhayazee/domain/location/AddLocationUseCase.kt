package com.nadhifhayazee.domain.location

import com.nadhifhayazee.data.repository.location.LocationRepository
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(location: MyLocation): Flow<ResultState<Boolean>> {
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