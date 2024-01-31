package com.nadhifhayazee.data.repository.location

import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.model.toEntity
import com.nadhifhayazee.data.source.room.model.toLocation
import com.nadhifhayazee.domain.model.Location
import com.nadhifhayazee.domain.repository.LocationRepository
import com.nadhifhayazee.shared.exceptions.DataNotFoundException
import com.nadhifhayazee.shared.exceptions.InsertDataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyLocationRepository @Inject constructor(
    private val locationDao: LocationDao
) : LocationRepository {

    override fun getLocations(): Flow<List<Location>> {
        return locationDao.getAll().map { locations ->
            locations.map {
                it.toLocation()
            }
        }

    }

    override suspend fun addLocation(location: Location): Boolean {
        return try {
            locationDao.insertLocation(location.toEntity())
            true
        } catch (e: Exception) {
            throw InsertDataException(e.message)
            false
        }
    }

    override suspend fun getLocationById(id: String): Location {
        return locationDao.getLocationById(id)?.toLocation()
            ?: throw DataNotFoundException("Wrong id")
    }

    override suspend fun deleteLocation(id: String) {
        return locationDao.deleteLocation(getLocationById(id).toEntity())
    }

}