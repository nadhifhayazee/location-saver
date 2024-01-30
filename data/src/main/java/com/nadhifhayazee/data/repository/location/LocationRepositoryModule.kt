package com.nadhifhayazee.data.repository.location

import com.nadhifhayazee.data.source.room.location.LocalLocationDataSource
import com.nadhifhayazee.data.source.room.location.LocalMyLocationDataSource
import com.nadhifhayazee.data.source.singleton.location.LocationSingletonDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object LocationRepositoryModule {

    @Provides
    fun provideMyLocationRepository(
        localMyLocationDataSource: LocalLocationDataSource
    ): LocationRepository = MyLocationRepository(localMyLocationDataSource)
}