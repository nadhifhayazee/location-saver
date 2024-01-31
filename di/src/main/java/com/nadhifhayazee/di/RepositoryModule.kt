package com.nadhifhayazee.di

import com.nadhifhayazee.data.repository.location.MyLocationRepository
import com.nadhifhayazee.data.source.room.dao.LocationDao
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


}