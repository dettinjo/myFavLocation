package de.hdm_stuttgart.myfavlocation.frontend.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hdm_stuttgart.myfavlocation.backend.repositories.SearchRepository

class SearchViewModelFactory(private val repository: SearchRepository)
    : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SearchViewModel(repository) as T
        }
}


