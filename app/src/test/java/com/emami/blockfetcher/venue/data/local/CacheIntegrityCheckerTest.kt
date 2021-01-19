package com.emami.blockfetcher.venue.data.local

import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.time.Instant

class CacheIntegrityCheckerTest {

    val localDataSource: VenueLocalDataSource = mock()
    val checker = CacheIntegrityChecker(localDataSource)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `should flag false when venue is cached for more than 7 days`() = runBlocking {

        whenever(localDataSource.getOldestVenueRecordCreationTime()).thenReturn(Instant.MIN)
        whenever(localDataSource.getCachedVenueCount()).thenReturn(1000)
        whenever(localDataSource.getLastKnownLocation()).thenReturn(null)

        assertFalse(checker.isDataValidForGivenQuery(LatitudeLongitude(1.1, 2.2)))
    }

    @Test
    fun `should flag true when venue is cached less than 7 days`() = runBlocking {

        whenever(localDataSource.getOldestVenueRecordCreationTime()).thenReturn(Instant.now())
        whenever(localDataSource.getCachedVenueCount()).thenReturn(1000)
        whenever(localDataSource.getLastKnownLocation()).thenReturn(null)

        assertTrue(checker.isDataValidForGivenQuery(LatitudeLongitude(1.1, 2.2)))
    }

    @Test
    fun `should flag false when cache size is less than default page size`() = runBlocking {

        whenever(localDataSource.getOldestVenueRecordCreationTime()).thenReturn(Instant.now())
        whenever(localDataSource.getCachedVenueCount()).thenReturn(Constants.DEFAULT_PAGE_SIZE - 1)
        whenever(localDataSource.getLastKnownLocation()).thenReturn(null)

        assertFalse(checker.isDataValidForGivenQuery(LatitudeLongitude(1.1, 2.2)))
    }

    @Test
    fun `should flag true when cache size is more than default page size`() = runBlocking {

        whenever(localDataSource.getOldestVenueRecordCreationTime()).thenReturn(Instant.now())
        whenever(localDataSource.getCachedVenueCount()).thenReturn(Constants.DEFAULT_PAGE_SIZE + 1)
        whenever(localDataSource.getLastKnownLocation()).thenReturn(null)

        assertTrue(checker.isDataValidForGivenQuery(LatitudeLongitude(1.1, 2.2)))
    }

    @Test
    fun `should flag true last known location is under 100 meters`() = runBlocking {

        whenever(localDataSource.getOldestVenueRecordCreationTime()).thenReturn(Instant.now())
        whenever(localDataSource.getCachedVenueCount()).thenReturn(Constants.DEFAULT_PAGE_SIZE + 1)
        whenever(localDataSource.getLastKnownLocation()).thenReturn(LatitudeLongitude(1.1, 2.2))

        assertTrue(checker.isDataValidForGivenQuery(LatitudeLongitude(1.1, 2.2)))
    }


    @Test
    fun `should flag true when current venue detail is null`() = runBlocking {

        whenever(localDataSource.getVenueDetailCreationTime("id")).thenReturn(null)

        assertTrue(checker.isCurrentVenueDetailValidByTime("id"))
    }

    @Test
    fun `should flag true when current venue detail is fresh`() = runBlocking {

        whenever(localDataSource.getVenueDetailCreationTime("id")).thenReturn(Instant.now())

        assertTrue(checker.isCurrentVenueDetailValidByTime("id"))
    }


}