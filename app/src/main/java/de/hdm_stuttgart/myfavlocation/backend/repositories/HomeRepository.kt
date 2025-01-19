package de.hdm_stuttgart.myfavlocation.backend.repositories

import android.os.CountDownTimer
import de.hdm_stuttgart.myfavlocation.backend.ServiceManager
import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnAPIResponse
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.RepositoryCommunication
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.OnDbFetch
import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.ServiceDependent
import de.hdm_stuttgart.myfavlocation.backend.data.UPDATE_INTERVAL
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.DatabaseRepository
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GooglePlacesRepository

class HomeRepository private constructor(
    private val googlePlacesRepository: GooglePlacesRepository,
    private val databaseRepository: DatabaseRepository): RepositoryCommunication, ServiceDependent{

    /**caching all nearby locations from the DB and the API
     * Updating every 30 seconds
     */
    var isInit: Boolean = false
    private lateinit var apiCallback: OnAPIResponse
    private lateinit var dbCallback: OnDbFetch
    private lateinit var communication: RepositoryCommunication

    //Timer Properties
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft = 0L

    /**
     * @param communication interface for generic repository-viewModel communication
     * @param apiCallback interface for api-specific communication
     * @param dbCallback interface for db-specific communication
     * multi-purpose function:
     * - initialize all the different communication interfaces
     * - initialize databaseCaches on application start
     * - start a continuous timer to fetch new data every 30 seconds
     * - perform an initial fetch from the google-places-api
     */
    fun initialize(communication: RepositoryCommunication, apiCallback: OnAPIResponse, dbCallback: OnDbFetch) {
        ServiceManager.getInstance().register(this)
        databaseRepository.updateCaches()
        isInit = true
        this.communication  = communication
        this.apiCallback = apiCallback
        this.dbCallback = dbCallback
        databaseRepository.communicationHome = this
        startTimer()
        getFresh()
    }

    /**
     *
     */
    private fun startTimer() {
        countDownTimer = object : CountDownTimer((UPDATE_INTERVAL).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            /**
             * Restart the timer when it finishes and refresh all the caches
             */
            override fun onFinish() {
                //Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
                getFresh()
                start() }
        }.start()
    }

    /**
     * get a fresh set of data from the google-places-API
     */
    fun getFresh() {
        communication.channel1()
        //Toast.makeText(context, "fresh", Toast.LENGTH_SHORT).show()
        googlePlacesRepository.searchRequestNearbySearch(apiCallback)
        databaseRepository.getAllInRadius(dbCallback)
    }

    /**
     * @return the cache from the googlePlacesRepository and the 'closest locations' cache from the databaseRepository
     *
     *
     */
    fun getCached(): List<Location>{
        val cachedLocations = mutableListOf<Location>()
        cachedLocations.addAll(databaseRepository.getCacheClosest())
        cachedLocations.addAll(googlePlacesRepository.getCache())
        return cachedLocations
    }

    override fun channel1() {
        getCached()
    }

    /**
     *
     */
    override fun onServiceGranted() {
        getFresh()
    }

    /**
     *
     */
    override fun onServiceDenied() {
    }

    companion object {
        @Volatile private var instance : HomeRepository? = null
        fun getInstance(googlePlacesRepository: GooglePlacesRepository,
                        databaseRepository: DatabaseRepository) =
            instance
                ?: synchronized(this){
                instance
                    ?: HomeRepository(googlePlacesRepository, databaseRepository)
                        .also { instance = it }
            }
    }
}
