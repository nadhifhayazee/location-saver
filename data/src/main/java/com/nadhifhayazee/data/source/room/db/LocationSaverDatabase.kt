package com.nadhifhayazee.data.source.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.dao.LocationImagesDao
import com.nadhifhayazee.data.source.room.model.LocationEntity
import com.nadhifhayazee.data.source.room.model.LocationImagesEntity

@Database(entities = [LocationEntity::class, LocationImagesEntity::class], version = 1)
abstract class LocationSaverDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun locationImageDao(): LocationImagesDao


    companion object {
        const val DB_NAME = "location_saver_db"
    }
}