package de.hdm_stuttgart.myfavlocation.backend.callback_interfaces

import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Location

/**
 * managing asynchronous responses from the google-places-api
 *
 */
interface OnAPIResponse {
    /**
     * @param results the resulting (transformed) list
     *
     * gets called, when api-call was successful
     */
    fun onSuccess(results: List<Location>)

    /**
     * @param errorCode the error-Code, (http-status-code)
     * @param url the url for which the error occurred
     *
     * gets invoked when api-call wasn't successful
     */
    fun onFailure(errorCode: String, url: String)
}