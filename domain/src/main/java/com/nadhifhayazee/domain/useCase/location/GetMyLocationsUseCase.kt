package com.nadhifhayazee.domain.useCase.location

import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.model.ResultState
import com.nadhifhayazee.domain.repository.LocationRepository
import com.nadhifhayazee.domain.useCase.locationImage.GenerateUriImagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyLocationsUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val generateUriImagesUseCase: GenerateUriImagesUseCase
) {

    operator fun invoke(): Flow<ResultState<List<Location>>> {
        return channelFlow {
            try {
//                send(ResultState.Loading())
                delay(300)
                locationRepository.getLocations().map {
                    it.map {
                        it.copy(locationUriImages = generateUriImagesUseCase(it.locationImages))
                    }

                }.collectLatest {
                    send(ResultState.Success(it))
                }

            } catch (e: Exception) {
                send(ResultState.Error(e))
            }

        }.flowOn(Dispatchers.IO)
    }

}