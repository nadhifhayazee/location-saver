package com.nadhifhayazee.locationsaver.screen.home

import com.nadhifhayazee.domain.model.Location

data class MyLocationsState(
    val isLoading: Boolean,
    val locations: List<Location>? = emptyList(),
    val throwable: Throwable? = null
)
