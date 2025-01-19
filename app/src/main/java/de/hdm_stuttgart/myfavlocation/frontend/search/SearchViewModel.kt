package de.hdm_stuttgart.myfavlocation.frontend.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnAPIResponse
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.repositories.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel(), OnAPIResponse, OnDbFetch {

    private val _places = MutableLiveData<List<Location>>()
    private var _error = MutableLiveData<String>()
    val locations: LiveData<List<Location>> = _places
    val error: LiveData<String> = _error
    private var storedValues = mutableListOf<Location>()

    /**
     * @param input the query for the search
     * send a searchRequest to the searchRepository
     */
    fun search(input: String) {
        searchRepository.search(this, this, input)
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
}