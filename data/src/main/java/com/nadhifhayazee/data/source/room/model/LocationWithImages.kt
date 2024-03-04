package com.nadhifhayazee.data.source.room.model

import androidx.room.Embedded
import androidx.room.Relation
import com.nadhifhayazee.domain.model.Location

data class LocationWithImages(
    @Embedded val location: LocationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "location_id"
    )
    val locationImages: List<LocationImagesEntity>
)

fun LocationWithImages.toLocation(): Location {
    return Location(
        id = this.location.id,
        name = this.location.name,
        locationDetail = this.location.locationDetail,
        longitude = this.location.longitude,
        latitude = this.location.latitude,
        locationImages = this.locationImages.map { it.name ?: "" }
    )
}