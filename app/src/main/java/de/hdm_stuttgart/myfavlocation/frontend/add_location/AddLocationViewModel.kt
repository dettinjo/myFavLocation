package de.hdm_stuttgart.myfavlocation.frontend.add_location

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.enumerations.Type
import de.hdm_stuttgart.myfavlocation.backend.repositories.AddLocationRepository

class AddLocationViewModel(private val repository: AddLocationRepository) : ViewModel() {

    fun getURL():String{
        return repository.getStaticMap()
    }

    /*
    safes location with current gps coordinates
     */
    fun saveLocation(image: String, name: String, description: String, category: String, rating: Float, context: Context){
        val position = repository.getPosition()
        if(position == null) {
           Toast.makeText(context, "Error: Location could not be saved",  Toast.LENGTH_LONG).show()
            }
        else {
            val location = Location(name, description, position, rating, image, Type.OWN, category)
            repository.createLocation(location)
        }
    }
}