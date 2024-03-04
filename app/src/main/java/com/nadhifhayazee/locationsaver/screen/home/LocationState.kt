package com.nadhifhayazee.locationsaver.screen.home

import com.nadhifhayazee.domain.model.Location

data class LocationState(
    val locations: List<Location> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)
