package com.nadhifhayazee.data.repository.location

import com.nadhifhayazee.shared.model.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocations(): Flow<List<MyLocation>>
    suspend fun addLocation(myLocation: MyLocation): Boolean
    suspend fun getLocationById(id: String): MyLocation
    suspend fun deleteLocation(id: String)
}