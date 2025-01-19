package de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db

import androidx.room.*
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(location: Location)

    @Update
    fun update(location: Location)

    @Query("DELETE FROM Location")
    fun deleteAll()

    @Query("SELECT * FROM Location")
    fun getAll() : List<Location>

    @Query("DELETE FROM Location WHERE position_lat = :lat AND position_lng = :lng")
    fun delete(lat: Double, lng: Double)

    @Query("SELECT * FROM Location WHERE identity = :id")
    fun getById(id: Int): List<Location>

    @Query("SELECT * FROM Location WHERE name LIKE :search")
    fun getByName(search: String) : List<Location>
}