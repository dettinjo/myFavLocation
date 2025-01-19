package de.hdm_stuttgart.myfavlocation.backend.repositories.detail

import de.hdm_stuttgart.myfavlocation.backend.data.PLACES_API_KEY
import de.hdm_stuttgart.myfavlocation.backend.data.SIZE
import de.hdm_stuttgart.myfavlocation.backend.data.ZOOM
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository

class LocationDetailRepository(private val databaseRepository: DatabaseRepository) {

    companion object {
        @Volatile private var instance : LocationDetailRepository? = null
        fun getInstance(databaseRepository: DatabaseRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: LocationDetailRepository(databaseRepository)
                            .also { instance = it }
                }
    }
    fun getStaticMap(location: String) : String{
        val marker = "color:red%7Clabel:%7C${location}"
        return "https://maps.googleapis.com/maps/api/staticmap?center=${location}&key=$PLACES_API_KEY&zoom=$ZOOM&size=$SIZE&markers=${marker}"
    }

    fun saveChanges(location: Location){
        databaseRepository.updateLocation(location)
    }

    fun addToFavorites(location: Location){
        databaseRepository.saveLocationToDB(location)
    }

    fun deleteFromFavorite(location: Location){
        databaseRepository.deleteLocation(location)
    }
    fun checkIfFavorite(location: Location): Boolean{
        return databaseRepository.contains(location)
    }
}