package de.hdm_stuttgart.myfavlocation.frontend.detail_views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdm_stuttgart.myfavlocation.backend.repositories.detail.LocationDetailRepository

class LocationDetailViewModelFactory(private val repository: LocationDetailRepository):
    ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocationDetailViewModel(repository) as T
        }

    }
