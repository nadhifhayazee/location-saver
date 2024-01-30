package com.nadhifhayazee.locationsaver.navigation

class LocationDetailNavigation {

    companion object {
        val route = "location/{LOCATION_ID_KEY}"
        fun createRoute(id: String) = "location/$id"
    }
}