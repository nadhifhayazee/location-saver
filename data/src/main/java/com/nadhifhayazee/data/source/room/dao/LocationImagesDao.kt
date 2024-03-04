package com.nadhifhayazee.data.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nadhifhayazee.data.source.room.model.LocationImagesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationImagesDao {

    @Insert
    suspend fun insertLocationImage(locationImagesEntity: LocationImagesEntity)

    @Query("SELECT * FROM location_images WHERE location_id = :locationId order by id DESC")
    fun getLocationImages(locationId: Int): Flow<List<LocationImagesEntity>>

}