package com.emami.blockfetcher.venue.model.domain

import com.emami.blockfetcher.venue.data.model.*
import org.junit.Test

class DomainTest {

    @Test
    fun venueTestShouldReturnCorrectDistanceInKilometer() {

        val distance = 1800
        val venue: Venue = Venue("", Location(LatitudeLongitude(.2, .2), "", distance), Category("",
            Icon(null, null, null, null)), "")

        assert(venue.labeledDistanceInKilometers.toLowerCase() == "1.80km")
    }

    @Test
    fun `Icon should construct correct path based on given size`() {
        val size = 56
        val prefix = "prefix/"
        val postFix = "/postFix"
        val icon = Icon(prefix, postFix, null, null)

        val expectedPath1 = "$prefix$size$postFix"
        assert(icon.getIconPathBySize(size) == expectedPath1)

        val expectedPath2 = "$prefix${size}x${size}$postFix"
        assert(icon.getIconPathByWidthHeight(size, size) == expectedPath2)
    }

    @Test
    fun `LatLng should concat points to single coordinate`() {
        val lat = 12.12
        val lng = 24.24
        val location = LatitudeLongitude(lat, lng)

        assert(location.coordinate == "$lat,$lng")
    }
}