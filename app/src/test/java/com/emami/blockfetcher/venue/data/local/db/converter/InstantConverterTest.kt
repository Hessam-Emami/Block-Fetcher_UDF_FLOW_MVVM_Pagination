package com.emami.blockfetcher.venue.data.local.db.converter

import junit.framework.Assert
import org.junit.Test
import java.time.Instant


class InstantConverterTest {
    private val dateConverter = InstantConverter()

    @Test
    fun `should successfully convert instant to long`() {
        val now = Instant.now()

        Assert.assertEquals(dateConverter.fromInstant(now), now.toEpochMilli())
    }

    @Test
    fun `should successfully convert back long to instant`() {
        val now = Instant.now()
        Assert.assertEquals(dateConverter.toInstant(now.toEpochMilli()), now)
    }
}