package com.nadhifhayazee.locationsaver.screen

sealed class Route(val route: String) {
    object Home : Route("Home")
    object CreateLocation : Route("create_location")
    object EditNote : Route("edit_note/{LOCATION_ID}") {
        fun createRoute(id: String): String {
            return "edit_note/$id"
        }
    }
    object EditTitle : Route("edit_title/{LOCATION_ID}") {
        fun createRoute(id: String): String {
            return "edit_title/$id"
        }
    }
}