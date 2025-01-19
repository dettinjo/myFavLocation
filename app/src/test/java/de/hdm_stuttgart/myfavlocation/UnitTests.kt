package de.hdm_stuttgart.myfavlocation

import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position
import de.hdm_stuttgart.myfavlocation.backend.repositories.master_repository.GPSRepository
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    /**
     * Calculates the proximity between two coordinates (rounded to meters)
     */
    @Test
    fun calculateProximityTest(){

        //Distance between Stuttgart and Ulm
        assertEquals(71166, GPSRepository.calculateProximity(
            Position(48.779301, 9.1071757),
            Position(48.3876817,9.8724053)))
        //Distance between Dresden and Brandenburg
        assertEquals(194390, GPSRepository.calculateProximity(
            Position(51.0769658, 13.6325036),
            Position(52.4553953,11.8948265)))
        //Distance between Duesseldorf and Berlin
        assertEquals(475031, GPSRepository.calculateProximity(
            Position(51.2385861,6.6742681),
            Position(52.5069704,13.2846498)))

    }
}
