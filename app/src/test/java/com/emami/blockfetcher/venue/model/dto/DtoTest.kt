package com.emami.blockfetcher.venue.model.dto

import com.emami.blockfetcher.venue.data.model.Dto
import org.junit.Test

class DtoTest {


    @Test
    fun `ExploreQuery should contain all inputs in its Map object`() {
        val expectedValues = listOf<String>("123,123", "1", "100", "200")
        val dto = Dto.ExploreQueryDto("123,123", 1, 100, 200)

        expectedValues.forEach { expected ->
            assert(dto.propertyMap.containsValue(expected))
        }
    }

    @Test
    fun `venue should return primary category from list of categories`() {
        val categories = listOf(Dto.Category("1", "name1", "", "", Dto.Icon("", "", 1, 1), false),
            Dto.Category("2", "name1", "", "", Dto.Icon("", "", 1, 1), false),
            Dto.Category("3", "name1", "", "", Dto.Icon("", "", 1, 1), true))
        val venue = Dto.VenueDTO(
            "", "",
            Dto.Location("", "", 1.1, 2.2, emptyList(), 12, "", "", "", "", "",
                emptyList()),
            categories, 2.2, Dto.VenuePage(123)
        )

        assert(venue.primaryCategory == categories[2])
    }

    @Test
    fun `venue should return first category if no primary exists`() {
        val categories = listOf(Dto.Category("1", "name1", "", "", Dto.Icon("", "", 1, 1), false),
            Dto.Category("2", "name1", "", "", Dto.Icon("", "", 1, 1), false),
            Dto.Category("3", "name1", "", "", Dto.Icon("", "", 1, 1), false))
        val venue = Dto.VenueDTO(
            "", "",
            Dto.Location("", "", 1.1, 2.2, emptyList(), 12, "", "", "", "", "",
                emptyList()),
            categories, 2.2, Dto.VenuePage(123)
        )

        assert(venue.primaryCategory == categories[0])
    }
}