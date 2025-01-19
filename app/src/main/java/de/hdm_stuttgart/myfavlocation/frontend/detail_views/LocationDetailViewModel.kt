package de.hdm_stuttgart.myfavlocation.frontend.detail_views

import androidx.lifecycle.ViewModel
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position
import de.hdm_stuttgart.myfavlocation.backend.repositories.detail.LocationDetailRepository

class LocationDetailViewModel(private val repository: LocationDetailRepository): ViewModel() {
    fun staticMap(position: Position): String{
        val location = "${position.lat}, ${position.lng}"
        return repository.getStaticMap(location)
    }

    fun saveChanges(location: Location){
        repository.saveChanges(location)
    }

    fun addToFavorite(location: Location){
        repository.addToFavorites(location)
    }

    fun deleteFromFavorites(location: Location){
        repository.deleteFromFavorite(location)
    }

    fun checkIfFavorite(location: Location): Boolean{
        return repository.checkIfFavorite(location)

    }
}