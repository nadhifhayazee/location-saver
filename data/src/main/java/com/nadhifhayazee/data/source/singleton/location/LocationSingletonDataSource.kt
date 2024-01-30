package com.nadhifhayazee.data.source.singleton.location


interface LocationSingletonDataSource {

    fun getSavedLocations(): MutableList<com.nadhifhayazee.shared.model.MyLocation>
    fun addLocation(myLocation: com.nadhifhayazee.shared.model.MyLocation): Boolean
    fun updateLocation(location: com.nadhifhayazee.shared.model.MyLocation): Boolean
    fun getLocationById(id: Int): com.nadhifhayazee.shared.model.MyLocation?
    fun deleteLocation(id: Int): Boolean
}