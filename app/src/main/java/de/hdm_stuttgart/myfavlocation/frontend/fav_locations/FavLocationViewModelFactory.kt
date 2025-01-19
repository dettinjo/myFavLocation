package de.hdm_stuttgart.myfavlocation.frontend.fav_locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdm_stuttgart.myfavlocation.backend.repositories.FavLocationRepository

class FavLocationViewModelFactory(private val repository: FavLocationRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavLocationViewModel(repository) as T
    }
}