package com.amazon.androidquickstartapp.ui.viewModel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.geoplaces.GeoPlacesClient
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.auth.awscredentials.CredentialsProvider
import aws.smithy.kotlin.runtime.net.url.Url
import com.amazon.androidquickstartapp.BuildConfig
import com.amazon.androidquickstartapp.R
import com.amazon.androidquickstartapp.utils.AmazonPlacesClient
import com.amazon.androidquickstartapp.utils.Constants
import com.amazon.androidquickstartapp.utils.Helper
import com.amazon.androidquickstartapp.utils.MapHelper
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponent
import org.maplibre.android.location.OnLocationCameraTransitionListener
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.maps.MapLibreMap
import kotlinx.coroutines.async
import software.amazon.location.auth.AuthHelper
import software.amazon.location.auth.LocationCredentialsProvider
import software.amazon.location.tracking.LocationTracker
import software.amazon.location.tracking.aws.LocationTrackingCallback
import software.amazon.location.tracking.config.LocationTrackerConfig

class MainViewModel : ViewModel() {
    var locationTracker: LocationTracker? = null
    var locationCredentialsProvider: LocationCredentialsProvider? = null
    var authenticated by mutableStateOf(false)
    var mapStyle by mutableStateOf(BuildConfig.MAP_STYLE)
    var region by mutableStateOf(BuildConfig.REGION)
    var identityPoolId by mutableStateOf(BuildConfig.IDENTITY_POOL_ID)
    var trackerName by mutableStateOf(BuildConfig.TRACKER_NAME)
    var label by mutableStateOf("")
    var isLocationTrackingForegroundActive: Boolean by mutableStateOf(false)
    var isFollowingLocationMarker: Boolean by mutableStateOf(false)
    var locationComponent: LocationComponent? = null
    var lastLocation: Location? = null
    var mapLibreMap: MapLibreMap? = null
    var helper = Helper()
    var mapHelper = MapHelper()
    var layerSize: Int = 0
    var getPlaceClient: GeoPlacesClient ?= null
    var amazonPlacesClient: AmazonPlacesClient ?= null

    suspend fun initializeLocationCredentialsProvider(authHelper: AuthHelper) {
        locationCredentialsProvider = viewModelScope.async {
            authHelper.authenticateWithCognitoIdentityPool(identityPoolId)
        }.await()
    }

    suspend fun initializeLocationTracker(
        context: Context,
        locationCredentialsProvider: LocationCredentialsProvider,
        config: LocationTrackerConfig
    ) {
        locationTracker = viewModelScope.async {
            LocationTracker(context, locationCredentialsProvider, config)
        }.await()
    }

    suspend fun reverseGeocode(latLng: LatLng): String? {
        try {
            if (getPlaceClient == null || amazonPlacesClient == null) {
                getPlaceClient =
                    GeoPlacesClient {
                        region = BuildConfig.API_KEY_REGION
                        endpointUrl = Url.parse("https://geo.${BuildConfig.API_KEY_REGION}.amazonaws.com/v2")
                        credentialsProvider = createEmptyCredentialsProvider()
                    }
                amazonPlacesClient = AmazonPlacesClient(getPlaceClient)
            }
            val response = amazonPlacesClient?.reverseGeocode(
                latLng.longitude,
                latLng.latitude,
                mLanguage = "en",
                mMaxResults = 1
            )

            return response?.resultItems?.firstOrNull()?.title
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun enableLocationComponent(context: Context) {
        mapLibreMap?.getStyle {
            locationComponent = mapLibreMap?.locationComponent
            mapHelper.enableLocationComponent(
                it, context, locationComponent
            )
            viewModelScope.launch {
                if (!checkLocationPermission(
                        context
                    )
                ) {
                    lastLocation =
                        locationTracker?.getDeviceLocation(null)
                    locationComponent?.forceLocationUpdate(lastLocation)
                    lastLocation?.let { location ->
                        mapLibreMap?.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(location.latitude, location.longitude))
                            .zoom(14.0)
                            .build()
                    }
                }
                setLiveTracking()
            }
        }
    }

    fun setLiveTracking() {
        locationComponent?.let {
            isFollowingLocationMarker = true
            it.setCameraMode(
                CameraMode.TRACKING,
                Constants.TRANSITION_DURATION,
                17.5,
                null,
                0.0,
                object : OnLocationCameraTransitionListener {
                    override fun onLocationCameraTransitionFinished(@CameraMode.Mode cameraMode: Int) {
                    }

                    override fun onLocationCameraTransitionCanceled(@CameraMode.Mode cameraMode: Int) {
                    }
                }
            )
        }
    }

    fun checkValidations(context: Context): Boolean {
        if (identityPoolId.isEmpty()) {
            helper.showToast(
                context.getString(R.string.error_please_enter_identity_pool_id),
                context
            )
            return true
        }
        if (mapStyle.isEmpty()) {
            helper.showToast(context.getString(R.string.error_please_enter_map_name), context)
            return true
        }
        if (region.isEmpty()) {
            helper.showToast(context.getString(R.string.error_please_enter_region), context)
            return true
        }
        return false
    }

    /**
     * Subscribes to location updates and updates the UI button text accordingly.
     */
    fun startTrackingForeground(
        context: Context,
        locationTrackingCallback: LocationTrackingCallback
    ) {
        if (checkLocationPermission(context)) return
        isLocationTrackingForegroundActive = true
        locationTracker?.start(locationTrackingCallback)
    }

    /**
     * Unsubscribes from location updates and updates the UI button text accordingly.
     */
    fun stopTrackingForeground(context: Context) {
        if (checkLocationPermission(context)) return
        isLocationTrackingForegroundActive = false
        locationTracker?.stop()
    }

    fun checkLocationPermission(context: Context) = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) != PackageManager.PERMISSION_GRANTED

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

    private fun createEmptyCredentialsProvider(): CredentialsProvider =
        StaticCredentialsProvider(
            Credentials.invoke(
                accessKeyId = "",
                secretAccessKey = "",
                sessionToken = null,
            ),
        )
}