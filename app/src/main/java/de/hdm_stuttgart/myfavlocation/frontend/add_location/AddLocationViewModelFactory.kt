package de.hdm_stuttgart.myfavlocation.frontend.add_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdm_stuttgart.myfavlocation.backend.repositories.AddLocationRepository

class AddLocationViewModelFactory(private val repository: AddLocationRepository):
    ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddLocationViewModel(repository) as T
        }
    }