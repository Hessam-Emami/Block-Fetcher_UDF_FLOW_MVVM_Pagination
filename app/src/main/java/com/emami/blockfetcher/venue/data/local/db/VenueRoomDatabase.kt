package com.emami.blockfetcher.venue.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emami.blockfetcher.venue.data.local.db.converter.InstantConverter
import com.emami.blockfetcher.venue.data.local.db.dao.RemoteKeysDao
import com.emami.blockfetcher.venue.data.local.db.dao.VenueDao
import com.emami.blockfetcher.venue.data.local.db.dao.VenueDetailDao
import com.emami.blockfetcher.venue.data.model.RemoteKeysEntity
import com.emami.blockfetcher.venue.data.model.VenueDetailEntity
import com.emami.blockfetcher.venue.data.model.VenueEntity

@Database(entities = [RemoteKeysEntity::class, VenueEntity::class, VenueDetailEntity::class],
    version = 2,
    exportSchema = false)
@TypeConverters(InstantConverter::class)
abstract class VenueRoomDatabase : RoomDatabase() {

    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun venueDao(): VenueDao
    abstract fun venueDetailDao(): VenueDetailDao

    companion object {
        @Volatile
        private var INSTANCE: VenueRoomDatabase? = null

        fun getDatabase(context: Context): VenueRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VenueRoomDatabase::class.java,
                    "venue_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}