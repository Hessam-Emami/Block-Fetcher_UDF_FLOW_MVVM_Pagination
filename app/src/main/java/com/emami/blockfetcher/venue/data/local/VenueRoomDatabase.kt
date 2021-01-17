package com.emami.blockfetcher.venue.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emami.blockfetcher.venue.data.local.dao.RemoteKeysDao
import com.emami.blockfetcher.venue.data.local.dao.VenueDao
import com.emami.blockfetcher.venue.data.model.RemoteKeysEntity
import com.emami.blockfetcher.venue.data.model.VenueEntity

@Database(entities = [RemoteKeysEntity::class, VenueEntity::class],
    version = 1,
    exportSchema = false)
abstract class VenueRoomDatabase : RoomDatabase() {

    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun venueDao(): VenueDao

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