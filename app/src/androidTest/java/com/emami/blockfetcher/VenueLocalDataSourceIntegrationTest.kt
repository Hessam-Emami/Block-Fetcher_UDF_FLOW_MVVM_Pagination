package com.emami.blockfetcher

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.emami.blockfetcher.venue.data.local.VenueLocalDataSource
import com.emami.blockfetcher.venue.data.local.db.VenueRoomDatabase
import com.emami.blockfetcher.venue.data.local.db.dao.RemoteKeysDao
import com.emami.blockfetcher.venue.data.local.db.dao.VenueDao
import com.emami.blockfetcher.venue.data.local.db.dao.VenueDetailDao
import com.emami.blockfetcher.venue.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VenueLocalDataSourceIntegrationTest {

    private lateinit var venueDao: VenueDao
    private lateinit var venueDetail: VenueDetailDao
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var db: VenueRoomDatabase
    private lateinit var pref: SharedPreferences
    private lateinit var localDataSource: VenueLocalDataSource

    @get:Rule
    val rule = CoroutineTestRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        pref = context.getSharedPreferences("TestSharedPref", Context.MODE_PRIVATE)
        db = Room.inMemoryDatabaseBuilder(
            context, VenueRoomDatabase::class.java).build()
        venueDao = db.venueDao()
        venueDetail = db.venueDetailDao()
        remoteKeysDao = db.remoteKeysDao()

        localDataSource = VenueLocalDataSource(db, pref)
        populateData()
    }

    @Test
    fun clearDataShouldNukeEverything() = rule.testDispatcher.runBlockingTest {
        localDataSource.invalidateData()

        assert(venueDao.getAllVenues().isEmpty())
        assert(venueDetail.getVenueById("3") == null)
        assert(remoteKeysDao.getRemoteKeyById("1") == null)
    }

    private fun populateData() = rule.testDispatcher.runBlockingTest {
        venueDao.insertAll(listOf(VenueEntity("1",
            "Venue",
            CategoryEntity("2", "Cat", "PREF", "POST"),
            LocationEntity(2.2, .2, "", 123))))
        venueDetail.insert(VenueDetailEntity("3",
            "VenueDet",
            "",
            "",
            "",
            "",
            2,
            null,
            RatingEntity(2.2, "", 2),
            CategoryEntity("2", "Cat", "PREF", "POST"),
            LocationEntity(2.2, .2, "", 123),
            null,
            null))
        remoteKeysDao.insertAll(listOf(RemoteKeysEntity("1", 25)))
    }

}