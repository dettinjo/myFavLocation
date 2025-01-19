package de.hdm_stuttgart.myfavlocation.backend.callback_interfaces

/**
 * enables reactions to permissions being granted/revoked from anywhere in the application
 */
interface ServiceDependent {
    /**
     * gets called when a permission is granted
     */
    fun onServiceGranted()

    /**
     * gets called when a permission is revoked
     */
    fun onServiceDenied()

}