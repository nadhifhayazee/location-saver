package com.nadhifhayazee.domain.location

import com.nadhifhayazee.data.repository.location.LocationRepository
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetMyLocationsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    operator fun invoke(): Flow<ResultState<List<MyLocation>>> {
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