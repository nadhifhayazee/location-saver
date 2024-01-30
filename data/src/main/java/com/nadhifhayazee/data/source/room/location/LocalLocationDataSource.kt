package com.nadhifhayazee.data.source.room.location

import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocalLocationDataSource {

    fun getAllLocations(): Flow<List<MyLocation>>
    suspend fun addLocation(location: MyLocation)
    suspend fun getLocationById(id: String): MyLocation?
    suspend fun deleteLocation(id: String)

}