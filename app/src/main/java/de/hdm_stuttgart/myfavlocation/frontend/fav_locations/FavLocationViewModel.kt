package de.hdm_stuttgart.myfavlocation.frontend.fav_locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.RepositoryCommunication
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.repositories.FavLocationRepository


class FavLocationViewModel(private val repository: FavLocationRepository) : ViewModel(), OnDbFetch, RepositoryCommunication {
    private val _places = MutableLiveData<List<Location>>()
    private var storedValues = mutableListOf<Location>()
    val locations: LiveData<List<Location>> = _places

    /**
     * initialize the favLocationsRepository
     */
    fun init(){
        repository.init(this)
    }

    /**
     * request data from the repository
     */
    fun pull() {
        storedValues = mutableListOf()
        _places.value = mutableListOf()
        repository.pull(this)
    }

    override fun dbFetch(results: List<Location>) {
        addLocations(results)
    }

    override fun channel1() {
        pull()
    }

    /**
     * @param results the list to be added
     * concatenate the given input-list with 'storedValues'
     * and post the new list as the vlaue of the MutableLiveData-object '_places'
     */
    private fun addLocations(results: List<Location>){
        storedValues.addAll(results)
        storedValues.sort()
        _places.postValue(storedValues)
    }
}