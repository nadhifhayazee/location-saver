package com.nadhifhayazee.locationsaver.screen.home

import com.nadhifhayazee.shared.model.MyLocation

data class MyLocationsState(
    val isLoading: Boolean,
    val locations: List<MyLocation>? = emptyList(),
    val throwable: Throwable? = null
)
