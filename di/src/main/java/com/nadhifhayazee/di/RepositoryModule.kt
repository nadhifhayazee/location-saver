package com.nadhifhayazee.di

import com.nadhifhayazee.data.repository.location.MyLocationImageRepository
import com.nadhifhayazee.data.repository.location.MyLocationRepository
import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.dao.LocationImagesDao
import com.nadhifhayazee.domain.repository.LocationImagesRepository
import com.nadhifhayazee.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMyLocationRepository(
        locationDao: LocationDao
    ): LocationRepository {
        return MyLocationRepository(locationDao)
    }


    @Provides
    fun provideMyLocationImageRepository(
        locationImagesDao: LocationImagesDao
    ): LocationImagesRepository {
        return MyLocationImageRepository(locationImagesDao)
    }
}