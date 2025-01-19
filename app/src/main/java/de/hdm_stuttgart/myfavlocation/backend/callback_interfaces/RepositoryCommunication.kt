package de.hdm_stuttgart.myfavlocation.backend.callback_interfaces

/**
 * This general purpose interface can be used for a parameter-less communication between different parts of
 * the application. More communication channels could be added if need be.
 *
 * Currently used for
 * - cross repository creation
 * - communication between the backend and frontend
 */
interface RepositoryCommunication {
    /**
     * communication-channel 1
     */
    fun channel1()
}