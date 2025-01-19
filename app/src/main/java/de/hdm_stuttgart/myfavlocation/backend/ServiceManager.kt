package de.hdm_stuttgart.myfavlocation.backend

import de.hdm_stuttgart.myfavlocation.backend.callback_interfaces.ServiceDependent

/**
 * used to notify different parts of the application on permission status changes.
 * This is very important, since many parts of the application might get loaded before permissions
 * are granted and therefore need to be somehow messaged, when the permission is finally given.
 *
 * Currently, this is a very rudimentary implementation, that doesn't distinguish between different
 * permissions. If the application gets larger, this scheme definitely needs to be a little more
 * sophisticated.
 */
class ServiceManager private constructor(){

    private val dependentList : MutableList<ServiceDependent> = mutableListOf()

    /**
     * notify every subscriber, that a permission was granted
     */
    fun notifyAllServicePermissionGranted(){
        for (dependent in dependentList) {
            dependent.onServiceGranted()
        }
    }

    /**
     * @param dependent
     * subscribe to the general list of dependents
     */
    fun register(dependent: ServiceDependent){
        dependentList.add(dependent)
    }

    companion object {
        @Volatile private var instance : ServiceManager? = null
        fun getInstance() =
            instance
                ?: synchronized(this){
                    instance
                        ?: ServiceManager()
                            .also { instance = it }
                }
    }
}