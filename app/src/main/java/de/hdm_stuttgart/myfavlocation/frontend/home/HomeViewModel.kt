package de.hdm_stuttgart.myfavlocation.frontend.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnAPIResponse
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.RepositoryCommunication
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.repositories.HomeRepository
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location

class HomeViewModel(private val repository: HomeRepository) : ViewModel(),
    OnAPIResponse, OnDbFetch, RepositoryCommunication {

    private var storedValues = mutableListOf<Location>()
    private val _places = MutableLiveData<MutableList<Location>>()
    val locations: LiveData<MutableList<Location>> = _places

    /**
     * @param results the list to be added
     * concatenate the given input-list with 'storedValues'
     * and post the new list as the vlaue of the MutableLiveData-object '_places'
     */
    private fun addLocations(results: List<Location>){
        storedValues.addAll(results)
        storedValues = storedValues.distinctBy { it.position }.toMutableList()
        storedValues.sort()
        _places.postValue(storedValues)
    }

    override fun onSuccess(results: List<Location>) {
        addLocations(results)
    }

    override fun onFailure(errorCode: String, url: String) {
    }

    override fun dbFetch(results: List<Location>) {
        addLocations(results)
    }

    /**
     * request data from the backend
     */
    fun get(){
        storedValues = mutableListOf()
        if(!repository.isInit){
            init()
        }
        else {
            getCached()
        }
    }

    /**
     * perform an initialization on the homeRepository and also perform an initial fetch
     */
    private fun init() {
        repository.initialize(this, this, this)
    }

    /**
     * get cached locations from the master-repositories
     */
    private fun getCached(){
        val caches = repository.getCached()
        addLocations(caches)
    }

    override fun channel1() {
        storedValues = mutableListOf()
        _places.value = mutableListOf()
    }
}

