package com.nadhifhayazee.data.source.room.location

import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.model.toEntity
import com.nadhifhayazee.data.source.room.model.toMyLocation
import com.nadhifhayazee.shared.exceptions.DataNotFoundException
import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalMyLocationDataSource @Inject constructor(
    private val locationDao: LocationDao
) : LocalLocationDataSource {

    override fun getAllLocations(): Flow<List<MyLocation>> {
        return locationDao.getAll().map { items ->
            items.map {
                it.toMyLocation()
            }
        }


    }

    override suspend fun addLocation(location: MyLocation) {
        locationDao.insertLocation(location.toEntity())
    }

    override suspend fun getLocationById(id: String): MyLocation? {
        return locationDao.getLocationById(id)?.toMyLocation()
    }

    override suspend fun deleteLocation(id: String) {
        val location = getLocationById(id)?.toEntity()
        if (location != null) {
            locationDao.deleteLocation(location)
        } else {
            throw DataNotFoundException("wrong id")
        }
    }
}