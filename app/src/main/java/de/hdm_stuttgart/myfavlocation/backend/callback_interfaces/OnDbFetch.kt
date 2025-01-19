package de.hdm_stuttgart.myfavlocation.backend.callback_interfaces

import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location

/**
 * managing asynchronous calls to the local db
 *
 */
interface OnDbFetch {
    /**
     * @param results
     * return a list containing Locations from the local db
     */
    fun dbFetch(results: List<Location>)
}