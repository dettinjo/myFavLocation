package de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location

@Database(version = 1, entities = [Location::class])
@TypeConverters(TypeConverter::class)
abstract class LocationDB: RoomDatabase() {

    companion object {
        fun get(context: Context) : LocationDB {
            return Room.databaseBuilder( context, LocationDB::class.java, "LocationDB")
                .fallbackToDestructiveMigration() //app is still in early development, so no important user-data has to be kept when changing the layout of the db
                .allowMainThreadQueries()
                .build()
        }
    }
    abstract fun getDao() : LocationDao
}

