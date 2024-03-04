package com.nadhifhayazee.data.source.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nadhifhayazee.data.source.room.model.LocationEntity
import com.nadhifhayazee.data.source.room.model.LocationWithImages
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDao {

    @Query("SELECT * FROM my_location ORDER BY id DESC")
    fun getAll(): Flow<List<LocationEntity>>

    @Transaction
    @Query("SELECT * FROM my_location ORDER BY id DESC")
    fun getLocationWithImages(): Flow<List<LocationWithImages>>

    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)

    @Update
    suspend fun updateLocation(locationEntity: LocationEntity)


    @Delete
    suspend fun deleteLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM MY_LOCATION WHERE id = :locationId")
    suspend fun getLocationById(locationId: String): LocationEntity?

}