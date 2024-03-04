package com.nadhifhayazee.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nadhifhayazee.domain.model.LocationImage

@Entity(tableName = "location_images")
data class LocationImagesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "location_id") val locationId: Int,

)


fun LocationImagesEntity.toLocationImage(): LocationImage {
    return LocationImage(
        id = this.id,
        name = this.name,
        locationId = this.locationId
    )
}

fun LocationImage.toEntity(): LocationImagesEntity {
    return LocationImagesEntity(
        id = this.id,
        name = this.name,
        locationId = this.locationId
    )
}