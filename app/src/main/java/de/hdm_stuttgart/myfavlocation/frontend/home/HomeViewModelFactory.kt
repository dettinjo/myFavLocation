package de.hdm_stuttgart.myfavlocation.frontend.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdm_stuttgart.myfavlocation.backend.repositories.HomeRepository

//this makes sure, that only one instance of a viewmodel can exist at a time

class HomeViewModelFactory(private val repository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }

}


