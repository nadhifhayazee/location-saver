package com.nadhifhayazee.data.repository.location

import com.nadhifhayazee.data.source.room.location.LocalLocationDataSource
import com.nadhifhayazee.shared.exceptions.DataNotFoundException
import com.nadhifhayazee.shared.exceptions.InsertDataException
import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyLocationRepository @Inject constructor(
    private val localMyLocationDataSource: LocalLocationDataSource
) : LocationRepository {

    override fun getLocations(): Flow<List<MyLocation>> {
        return localMyLocationDataSource.getAllLocations()

    }

    override suspend fun addLocation(myLocation: MyLocation): Boolean {
        return try {
            localMyLocationDataSource.addLocation(myLocation)
            true
        } catch (e: Exception) {
            throw InsertDataException(e.message)
            false
        }
    }

    override suspend fun getLocationById(id: String): MyLocation {
        return localMyLocationDataSource.getLocationById(id)
            ?: throw DataNotFoundException("Wrong id")
    }

    override suspend fun deleteLocation(id: String) {
        return localMyLocationDataSource.deleteLocation(id)
    }

}