package com.nadhifhayazee.data.source.singleton.location

import com.nadhifhayazee.shared.model.MyLocation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationSingletonModule {

    @Provides
    @Singleton
    fun provideMySavedLocations() = mutableListOf<com.nadhifhayazee.shared.model.MyLocation>()

    @Provides
    @Singleton
    fun provideMyLocationSingletonDataSource(
        myLocations: MutableList<com.nadhifhayazee.shared.model.MyLocation>
    ): LocationSingletonDataSource {
        return MyLocationSingletonDataSource(myLocations)
    }
}