package com.nadhifhayazee.data.source.singleton.location

import javax.inject.Inject

class MyLocationSingletonDataSource @Inject constructor(
    private val myLocations: MutableList<com.nadhifhayazee.shared.model.MyLocation>
) : LocationSingletonDataSource {


    override fun getSavedLocations(): MutableList<com.nadhifhayazee.shared.model.MyLocation> {
        return myLocations
    }

    override fun addLocation(myLocation: com.nadhifhayazee.shared.model.MyLocation): Boolean {
        if (myLocations.any { it.id == myLocation.id }) return false
        myLocations.add(myLocation)
        return true
    }

    override fun updateLocation(location: com.nadhifhayazee.shared.model.MyLocation): Boolean {
        if (!myLocations.any { it.id == location.id }) return false
        myLocations[myLocations.indexOf(location)] = location
        return true
    }

    override fun getLocationById(id: Int): com.nadhifhayazee.shared.model.MyLocation? {
        return myLocations.find { it.id == id }
    }

    override fun deleteLocation(id: Int): Boolean {
        return myLocations.remove(myLocations.find { it.id == id })
    }
}