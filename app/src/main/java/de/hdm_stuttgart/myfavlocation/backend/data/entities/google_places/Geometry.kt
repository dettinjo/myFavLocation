package de.hdm_stuttgart.myfavlocation.backend.data.entities.google_places

import de.hdm_stuttgart.myfavlocation.backend.data.entities.plc_location_db.Position

class Geometry {
    lateinit var location: Position
    private lateinit var viewport: Viewport
}