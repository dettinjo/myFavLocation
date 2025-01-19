package de.hdm_stuttgart.myfavlocation.backend.repositories

import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnAPIResponse
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GooglePlacesRepository

class SearchRepository private constructor(
    private val googlePlacesRepository: GooglePlacesRepository,
    private val databaseRepository: DatabaseRepository) {

    /**
     * @param placesCallback
     * @param dbCallback
     * @param searchEntry
     */
    fun search(placesCallback: OnAPIResponse, dbCallback: OnDbFetch, searchEntry : String) {
        databaseRepository.getSpecific(dbCallback, searchEntry)
        googlePlacesRepository.searchRequestNamedSearch(placesCallback, searchEntry)
    }

    companion object {
        @Volatile private var instance : SearchRepository? = null
        fun getInstance(googlePlacesRepository: GooglePlacesRepository, databaseRepository: DatabaseRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: SearchRepository(googlePlacesRepository, databaseRepository)
                            .also { instance = it }
                }
    }
}


