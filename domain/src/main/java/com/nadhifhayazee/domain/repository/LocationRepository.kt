package com.nadhifhayazee.domain.repository

import com.nadhifhayazee.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocations(): Flow<List<Location>>
    suspend fun addLocation(location: Location): Boolean
    suspend fun getLocationById(id: String): Location
    suspend fun updateLocation(location: Location)
    suspend fun deleteLocation(id: String)
}