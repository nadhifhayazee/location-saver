package com.nadhifhayazee.locationsaver.screen.home

import com.nadhifhayazee.domain.model.Location

data class LocationState(
    val locations: List<Location>? = null,
    val loading: Boolean = true,
    val errorMessage: String? = null
)
