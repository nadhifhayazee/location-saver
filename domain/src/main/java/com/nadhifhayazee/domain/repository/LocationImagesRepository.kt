package com.nadhifhayazee.domain.repository

import com.nadhifhayazee.domain.model.LocationImage
import kotlinx.coroutines.flow.Flow

interface LocationImagesRepository {

   suspend fun addLocationImage(locationImage: LocationImage): Boolean

    fun getLocationImages(locationId: Int): Flow<List<LocationImage>>
}