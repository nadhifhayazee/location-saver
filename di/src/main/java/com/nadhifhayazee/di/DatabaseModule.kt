package com.nadhifhayazee.di

import android.content.Context
import androidx.room.Room
import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.dao.LocationImagesDao
import com.nadhifhayazee.data.source.room.db.LocationSaverDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLocationSaverDatabase(
        @ApplicationContext context: Context
    ): LocationSaverDatabase {
        return Room.databaseBuilder(
            context,
            LocationSaverDatabase::class.java,
            LocationSaverDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(
        locationSaverDatabase: LocationSaverDatabase
    ): LocationDao {
        return locationSaverDatabase.locationDao()
    }

    @Provides
    @Singleton
    fun provideLocationImageDao(
        locationSaverDatabase: LocationSaverDatabase
    ): LocationImagesDao {
        return locationSaverDatabase.locationImageDao()
    }
}