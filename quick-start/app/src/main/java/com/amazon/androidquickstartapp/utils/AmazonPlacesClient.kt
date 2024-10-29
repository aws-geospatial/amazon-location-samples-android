package com.amazon.androidquickstartapp.utils


import aws.sdk.kotlin.services.geoplaces.GeoPlacesClient
import aws.sdk.kotlin.services.geoplaces.model.ReverseGeocodeRequest
import aws.sdk.kotlin.services.geoplaces.model.ReverseGeocodeResponse
import com.amazon.androidquickstartapp.BuildConfig

/**
 * Provides methods to interact with the Amazon Location service.
 *
 * @property geoPlacesClient An instance of LocationClient used for making requests to the Amazon Location service.
 */
class AmazonPlacesClient(
    private val geoPlacesClient: GeoPlacesClient?
) {

    /**
     * Reverse geocodes a location specified by longitude and latitude coordinates.
     *
     * @param longitude The longitude of the location to reverse geocode.
     * @param latitude The latitude of the location to reverse geocode.
     * @param mLanguage The language to use for the reverse geocoding results.
     * @param mMaxResults The maximum number of results to return.
     * @return A response containing the reverse geocoding results.
     */
    suspend fun reverseGeocode(
        longitude: Double,
        latitude: Double,
        mLanguage: String,
        mMaxResults: Int
    ): ReverseGeocodeResponse? {
        val request = ReverseGeocodeRequest {
            maxResults = mMaxResults
            language = mLanguage
            key = BuildConfig.API_KEY
            queryPosition = listOf(longitude, latitude)
        }

        val response = geoPlacesClient?.reverseGeocode(request)
        return response
    }
}
