package com.nadhifhayazee.data.repository.location

import com.nadhifhayazee.data.source.room.dao.LocationImagesDao
import com.nadhifhayazee.data.source.room.model.toEntity
import com.nadhifhayazee.domain.model.LocationImage
import com.nadhifhayazee.domain.repository.LocationImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyLocationImageRepository @Inject constructor(
    private val locationImagesDao: LocationImagesDao
) : LocationImagesRepository{
    override suspend fun addLocationImage(locationImage: LocationImage): Boolean {
        locationImagesDao.insertLocationImage(locationImage.toEntity())
        return true
    }

    override fun getLocationImages(locationId: Int): Flow<List<LocationImage>> {
        TODO("Not yet implemented")
    }
}