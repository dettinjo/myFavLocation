package de.hdm_stuttgart.myfavlocation.backend.repositories

import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.RepositoryCommunication
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository

class FavLocationRepository private constructor(private val databaseRepository: DatabaseRepository) {

    /**
     * initialize this repository
     */
    fun init(repositoryCommunication: RepositoryCommunication){
        databaseRepository.communicationFav = repositoryCommunication
    }

    /**
     * fetch everything from the local db
     */
    fun pull(callback: OnDbFetch){
        databaseRepository.getAll(callback)
    }
    
    companion object {
        @Volatile private var instance : FavLocationRepository? = null
        fun getInstance(databaseRepository: DatabaseRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: FavLocationRepository(databaseRepository)
                            .also { instance = it }
                }
    }
}