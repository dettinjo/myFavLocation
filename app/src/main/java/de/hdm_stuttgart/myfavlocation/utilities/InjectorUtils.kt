package de.hdm_stuttgart.myfavlocation.utilities

import android.content.Context
import de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db.LocationDB
import de.hdm_stuttgart.myfavlocation.backend.repositories.AddLocationRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.FavLocationRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.HomeRepository.Companion.getInstance
import de.hdm_stuttgart.myfavlocation.backend.repositories.SearchRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.detail.LocationDetailRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GPSRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GooglePlacesRepository
import de.hdm_stuttgart.myfavlocation.frontend.add_location.AddLocationViewModelFactory
import de.hdm_stuttgart.myfavlocation.frontend.detail_views.LocationDetailViewModelFactory
import de.hdm_stuttgart.myfavlocation.frontend.fav_locations.FavLocationViewModelFactory
import de.hdm_stuttgart.myfavlocation.frontend.home.HomeViewModelFactory
import de.hdm_stuttgart.myfavlocation.frontend.search.SearchViewModelFactory

/**
 * All dependency chains are managed in this centralized location. Components can be swapped out
 * for testing purposes or for different implementations
 *
 * The highest link of the dependency chain is in any case the ViewModelFactory, so the different
 * functions build it's dependencies up step by step, by grabbing references to the repositories
 * (which are all implemented as singletons) and finally returning the ViewModelFactory
 */
object InjectorUtils {

    fun provideHomeViewModelFactory(context: Context) : HomeViewModelFactory {
        val gpsRepository = GPSRepository.getInstance(context)
        val databaseRepository = DatabaseRepository.getInstance(
            LocationDB.get(context).getDao(),
            gpsRepository)
        val googlePlacesRepository =
            GooglePlacesRepository.getInstance(gpsRepository)
        val homeRepository =
            getInstance(googlePlacesRepository, databaseRepository)
        return HomeViewModelFactory(homeRepository)
    }

    fun provideSearchViewModelFactory(context: Context) : SearchViewModelFactory {
        val gpsRepository = GPSRepository.getInstance(context)
        val databaseRepository = DatabaseRepository.getInstance(LocationDB.get(context).getDao(), gpsRepository)
        val googlePlacesRepository = GooglePlacesRepository.getInstance(gpsRepository)
        val searchRepository = SearchRepository.getInstance(googlePlacesRepository, databaseRepository)
        return SearchViewModelFactory(searchRepository)
    }

    fun provideAddLocationViewModelFactory(context: Context) : AddLocationViewModelFactory{
        val gpsRepository = GPSRepository.getInstance(context)
        val databaseRepository = DatabaseRepository.getInstance(LocationDB.get(context).getDao(), gpsRepository)
        val addLocationRepository = AddLocationRepository.getInstance(gpsRepository,databaseRepository)
        return AddLocationViewModelFactory(addLocationRepository)
    }

    fun provideFavLocationsViewModelFactory(context: Context) : FavLocationViewModelFactory {
        val gpsRepository = GPSRepository.getInstance(context)
        val databaseRepository = DatabaseRepository.getInstance(LocationDB.get(context).getDao(), gpsRepository)
        val favLocationRepository = FavLocationRepository.getInstance(databaseRepository)
        return FavLocationViewModelFactory(favLocationRepository)
    }

    fun provideLocationsDetailViewModelFactory(context: Context) : LocationDetailViewModelFactory {
        val gpsRepository = GPSRepository.getInstance(context)
        val databaseRepository = DatabaseRepository.getInstance(LocationDB.get(context).getDao(), gpsRepository)
        val locationDetailRepository = LocationDetailRepository.getInstance(databaseRepository)
        return LocationDetailViewModelFactory(locationDetailRepository)
    }
}