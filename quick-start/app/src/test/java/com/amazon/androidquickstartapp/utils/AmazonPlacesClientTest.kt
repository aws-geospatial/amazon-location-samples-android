
package com.amazon.androidquickstartapp.utils

import android.content.Context
import aws.sdk.kotlin.services.geoplaces.GeoPlacesClient
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.runs
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import software.amazon.location.auth.EncryptedSharedPreferences
import software.amazon.location.auth.utils.Constants

class AmazonPlacesClientTest {

    private lateinit var context: Context
    private lateinit var mockLocationClient: GeoPlacesClient
    private lateinit var amazonLocationClient: AmazonPlacesClient
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        mockLocationClient = mockk(relaxed = true)
        mockkConstructor(EncryptedSharedPreferences::class)

        every { anyConstructed<EncryptedSharedPreferences>().initEncryptedSharedPreferences() } just runs
        every { anyConstructed<EncryptedSharedPreferences>().put(any(), any<String>()) } just runs
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.REGION) } returns "us-east-1"
        every { anyConstructed<EncryptedSharedPreferences>().clear() } just runs

    }

    @Test
    fun `test reverseGeocode with valid response`() {
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.METHOD) } returns "cognito"
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.ACCESS_KEY_ID) } returns "test"
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.SECRET_KEY) } returns "test"
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.SESSION_TOKEN) } returns "test"
        every { anyConstructed<EncryptedSharedPreferences>().get(Constants.EXPIRATION) } returns "11111"
        amazonLocationClient = AmazonPlacesClient(mockLocationClient)
        coroutineScope.launch {
            amazonLocationClient.reverseGeocode(23.151, 27.262, "en", 10)
            assertNotNull(amazonLocationClient)
        }
    }

}
