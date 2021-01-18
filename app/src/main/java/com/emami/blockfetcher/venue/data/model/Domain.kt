package com.emami.blockfetcher.venue.data.model

import androidx.annotation.Px

data class Venue(
    val id: String,
    val location: Location,
    val primaryCategory: Category,
    val name: String,
) {
    val labeledDistanceInKilometers: String
        get() = buildString {
            val distanceInKilo = location.distance.toDouble() / 1000
            val formattedDistance = String.format("%.2f", distanceInKilo)
            append(formattedDistance)
            append("km")
        }

    val address: String
        get() = location.address
    val tag: String
        get() = primaryCategory.name
}

data class Category(
    val name: String,
    private val iconPrefix: String,
    private val iconPostFix: String,
) {
    fun icon(@Px widthHeight: Int) = buildString {
        append(iconPostFix)
        append(widthHeight)
        append(iconPostFix)
    }
}

data class Location(
    val coordinate: LatitudeLongitude,
    val address: String, val distance: Int,
)

data class LatitudeLongitude(val lat: Double, val lng: Double) {
    val coordinate: String
        get() = buildString {
            append(lat)
            append(',')
            append(lng)
        }
}
