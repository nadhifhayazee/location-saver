package com.nadhifhayazee.locationsaver.screen.home

sealed class LocationEvent {
    data class DeleteLocation(val id: String) : LocationEvent()
}