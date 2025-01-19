package de.hdm_stuttgart.myfavlocation.backend.repositories

import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position
import de.hdm_stuttgart.myfavlocation.backend.data.PLACES_API_KEY
import de.hdm_stuttgart.myfavlocation.backend.data.SIZE
import de.hdm_stuttgart.myfavlocation.backend.data.ZOOM
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GPSRepository



class AddLocationRepository private constructor(
    private val gpsRepository: GPSRepository,
    private val databaseRepository: DatabaseRepository) {

    /*
    Builds static map request with inserted coordinates
     */
    fun getStaticMap() : String {
        val position = gpsRepository.getCurrentPosition()

        if (position != null) {
            val coordinateString = "${position.lat},${position.lng}"
            val marker = "color:red%7Clabel:%7C${coordinateString}"
            return "https://maps.googleapis.com/maps/api/staticmap?center=${coordinateString}&key=$PLACES_API_KEY&zoom=$ZOOM&size=$SIZE&markers=${marker}"
        }
        return ""

    }

    /*
    Gets current GPS position from gpsRepository
     */
    fun getPosition(): Position?{
        return gpsRepository.getCurrentPosition()
    }

    /*
    creates location
     */
    fun createLocation(location: Location){
        databaseRepository.saveLocationToDB(location)
    }

    companion object {
        @Volatile private var instance : AddLocationRepository? = null
        fun getInstance(gpsRepository: GPSRepository, databaseRepository: DatabaseRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: AddLocationRepository(gpsRepository, databaseRepository)
                            .also { instance = it }
                }
    }
}