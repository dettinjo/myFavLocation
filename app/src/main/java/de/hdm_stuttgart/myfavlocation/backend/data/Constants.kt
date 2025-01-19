package de.hdm_stuttgart.myfavlocation.backend.data

/**
 * Constants concerning the Google-Places API
 */
const val BASE_URL = "https://maps.googleapis.com/"
const val PLACES_API_KEY = "AIzaSyBX9CqV8aRYIR7YoNHXLTq9xOuwmziMMhM"

/**
 * Radius of the earth in meters, used to calculate the distance between two coordinates
 */
const val R = 6371000

/**
 * Fow frequently new data is fetched from the Google-Places-API
 */
const val UPDATE_INTERVAL = 30000

/**
 * The radius, how far a location can be and still be shown to the user
 */
const val RADIUS = 200

/**
 * Values concerning the size and scope of the image, returned by the Google-Static-Maps-API
 */
const val SIZE = "400x400"
const val ZOOM = "18"

/**
 * Permission-Codes used to handle the results of the permissions
 */
const val ACCESS_FINE_LOCATION_CODE = 1
const val REQUEST_IMAGE_CAPTURE = 2