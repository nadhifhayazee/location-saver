package com.nadhifhayazee.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nadhifhayazee.domain.model.Location

@Entity(tableName = "my_location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "location_detail") val locationDetail: String?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "latitude") val latitude: Double?
)

fun LocationEntity.toLocation(): Location {
    return Location(
        id = this.id,
        name = this.name,
        locationDetail = this.locationDetail,
        longitude = this.longitude,
        latitude = this.latitude
    )
}

fun Location.toEntity(): LocationEntity{
    return LocationEntity(
        id = this.id,
        name = this.name,
        locationDetail = this.locationDetail,
        longitude = this.longitude,
        latitude = this.latitude
    )
}