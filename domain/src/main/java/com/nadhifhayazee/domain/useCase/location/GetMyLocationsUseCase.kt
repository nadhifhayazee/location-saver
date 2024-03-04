package com.nadhifhayazee.domain.useCase.location

import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetMyLocationsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    operator fun invoke(): Flow<ResultState<List<Location>>> {
        return channelFlow {
            try {
                send(ResultState.Loading())
                delay(500)
                locationRepository.getLocations().collectLatest {
                    send(ResultState.Success(it))
                }

            } catch (e: Exception) {
                send(ResultState.Error(e))
            }

        }
    }

}