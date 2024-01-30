package com.nadhifhayazee.data.source.room.location

import com.nadhifhayazee.data.source.room.dao.LocationDao
import com.nadhifhayazee.data.source.room.db.LocationSaverDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object LocalLocationDataSourceModule {

    @Provides
    fun provideLocalMyLocationDataSource(
        locationDao: LocationDao
    ): LocalLocationDataSource {
        return LocalMyLocationDataSource(locationDao)
    }
}