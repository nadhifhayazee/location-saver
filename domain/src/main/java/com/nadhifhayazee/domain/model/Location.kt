package com.nadhifhayazee.domain.model

data class Location(
    val id: Int = 0,
    val name: String?,
    val locationDetail: String?,
    val longitude: Double?,
    val latitude: Double?,
    val locationImages: List<String> = emptyList()
)

