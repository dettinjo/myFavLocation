package de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository

import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.RepositoryCommunication
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.data.RADIUS
import de.hdm_stuttgart.myfavlocation.backend.data.fav_location_db.LocationDao

/**
 * @param dao
 * @param gpsRepository direct reference to the GPS-master Repository
 * @property dbCacheClosest cache for storing all locations from the db within a certain radius of the user
 * @property dbCache cache for own locations stored in the db
 * @property communicationHome communication interface for the Home-Fragment
 * @property communicationFav communication interface for the FavLocations-Fragment
 *
 * Handles all the different interactions with the locations stored within the local db
 *
 */
class DatabaseRepository private constructor(
    private val dao : LocationDao,
    private val gpsRepository: GPSRepository) {

    //caching the closest locations
    private var dbCacheClosest: MutableList<Location> = mutableListOf()

    //caching all other locations
    private var dbCache: List<Location> = mutableListOf()

    lateinit var communicationHome: RepositoryCommunication
    lateinit var communicationFav: RepositoryCommunication

    /**
     * @param callback
     * find every location within a certain radius.
     * cache-validity is checked first to avoid unnecessary calls to the database
     */
    fun getAllInRadius(callback: OnDbFetch) {
        val list = mutableListOf<Location>()

        val currentPosition = gpsRepository.getCurrentPosition()

            list.addAll(dbCache)

            if(currentPosition != null) {
                gpsRepository.calculateProximity(currentPosition, list)
            }
            val final = mutableListOf<Location>()
            list.forEach {
                if(it.localProximity <= RADIUS){
                    final.add(it)
                }
            }
            dbCacheClosest = final
            callback.dbFetch(dbCacheClosest)

    }

    /**
     * @param location the location to check
     * @return the result of the check
     *
     * check, if the db contains a certain location or not
     *
     */
    fun contains(location: Location): Boolean {
        return dao.getById(location.identity).isNotEmpty()
    }

    /**
     * renew all caches by fetching locations from the db (dbCacheOwn, dbCacheGoogle) and by
     * recalculating the distance to these locations (dbCacheClosest)
     */
    fun updateCaches() {
        dbCache = dao.getAll()
        cacheAllInRadius()
    }

    /**
     * get the locations from dbCacheOwn and dbCacheGoogle and calculate the distance to all of them.
     * result will be stored in dbCacheClosest
     */
    private fun cacheAllInRadius() {
        val currentPosition = gpsRepository.getCurrentPosition()
        val list = mutableListOf<Location>()

            list.addAll(dbCache)

            if(currentPosition != null) {
                gpsRepository.calculateProximity(currentPosition, list)
            }
            val final = mutableListOf<Location>()
            list.forEach {
                if(it.localProximity <= RADIUS){
                    final.add(it)
                }
            }
            dbCacheClosest = final

    }

    /**
     * @return dbCacheClosest
     */
    fun getCacheClosest(): List<Location>{
        cacheAllInRadius()
        return dbCacheClosest
    }

    /**
     * @param callback
     * @param search
     */
    fun getSpecific(callback : OnDbFetch, search : String){
        val modSearch = "%$search%"
        callback.dbFetch(dao.getByName(modSearch))
    }

    /**
     * @param callback
     */
    fun getAll(callback : OnDbFetch) {
        callback.dbFetch(dbCache)
    }

    /**
     * @param location the location to be inserted
     */
    fun saveLocationToDB(location: Location){
        dao.insert(location)

        updateOnChange()
    }

    /**
     * @param location the location to be updated
     */
    fun updateLocation(location: Location){
        dao.update(location)

        updateOnChange()
    }

    /**
     * @param location the location to be deleted
     *
     * delete a location and invalidate the corresponding cache
     */
    fun deleteLocation(location: Location){
        dao.delete(location.position!!.lat, location.position.lng)

        updateOnChange()
    }

    /**
     * gets called whenever a change happens to the dataset
     * Fragments will be messaged and all of the caches will be updated
     */
    private fun updateOnChange(){
        updateCaches()

        if(this::communicationHome.isInitialized){
            communicationHome.channel1()
        }
        if(this::communicationFav.isInitialized){
            communicationFav.channel1()
        }
    }

    companion object {
        @Volatile private var instance : DatabaseRepository? = null
        fun getInstance(dao : LocationDao, gpsRepository: GPSRepository) =
            instance
                ?: synchronized(this){
                    instance
                        ?: DatabaseRepository(dao, gpsRepository)
                            .also { instance = it }
                }
    }
}