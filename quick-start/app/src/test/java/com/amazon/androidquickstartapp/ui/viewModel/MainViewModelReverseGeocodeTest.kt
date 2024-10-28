package com.amazon.androidquickstartapp.ui.viewModel

import android.content.Context
import aws.sdk.kotlin.services.geoplaces.GeoPlacesClient
import aws.sdk.kotlin.services.geoplaces.model.Address
import aws.sdk.kotlin.services.geoplaces.model.PlaceType
import aws.sdk.kotlin.services.geoplaces.model.ReverseGeocodeResponse
import aws.sdk.kotlin.services.geoplaces.model.ReverseGeocodeResultItem
import com.amazon.androidquickstartapp.utils.AmazonPlacesClient
import com.amazon.androidquickstartapp.utils.Constants.EXPECTED_LABEL
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkConstructor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.maplibre.android.geometry.LatLng
import org.mockito.Mock
import software.amazon.location.auth.LocationCredentialsProvider


class MainViewModelReverseGeocodeTest {

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var geoPlacesClient: GeoPlacesClient

    @Mock
    lateinit var context: Context

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        geoPlacesClient = mockk()
        viewModel = MainViewModel()
        mockkConstructor(LocationCredentialsProvider::class)
        mockkConstructor(AmazonPlacesClient::class)
    }

    @Test
    fun `test reverseGeocode`() {
        val mockAmazonLocationClient = mockk<AmazonPlacesClient>()
        viewModel.getPlaceClient = geoPlacesClient
        viewModel.amazonPlacesClient = mockAmazonLocationClient
        val searchPlaceIndexForPositionResponse = ReverseGeocodeResponse {
            resultItems = listOf(ReverseGeocodeResultItem {
                distance = 20L
                placeId = "11"
                title = "test"
                placeType = PlaceType.Block
                address = Address {
                    label = EXPECTED_LABEL
                }
            })
            pricingBucket = "test"
        }
        coEvery { mockAmazonLocationClient.reverseGeocode(any(), any(), any(), any()) } returns searchPlaceIndexForPositionResponse
        val latLng = LatLng(37.7749, -122.4194)

        runBlocking {
            val label = viewModel.reverseGeocode(latLng)

            assertEquals(EXPECTED_LABEL, label)
        }
    }
}