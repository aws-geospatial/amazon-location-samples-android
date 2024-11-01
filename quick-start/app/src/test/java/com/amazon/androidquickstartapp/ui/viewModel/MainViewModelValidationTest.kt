package com.amazon.androidquickstartapp.ui.viewModel

import android.content.Context
import com.amazon.androidquickstartapp.utils.Constants.IDENTITY_POOL_ID
import com.amazon.androidquickstartapp.utils.Constants.MAP_STYLE
import com.amazon.androidquickstartapp.utils.Constants.API_KEY_REGION
import com.amazon.androidquickstartapp.utils.Constants.API_KEY
import com.amazon.androidquickstartapp.utils.Constants.TRACKER_NAME
import com.amazon.androidquickstartapp.utils.Helper
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


class MainViewModelValidationTest {

    private lateinit var viewModel: MainViewModel

    @Mock
    lateinit var context: Context

    @Mock
    private lateinit var mockHelper: Helper

    @Before
    fun setUp() {
        mockHelper = mockk()
        context = mockk(relaxed = true)
        viewModel = MainViewModel()
        viewModel.helper = mockHelper
    }

    @Test
    fun `test checkValidations when all fields are empty`() {
        every { mockHelper.showToast(any(), any()) } just runs
        viewModel.identityPoolId = ""
        viewModel.mapStyle  = ""
        viewModel.apiKey = ""
        viewModel.apiKeyRegion = ""
        viewModel.trackerName = ""

        assertEquals(true, viewModel.checkValidations(context))
    }

    @Test
    fun `test checkValidations when mapStyle is empty`() {
        every { mockHelper.showToast(any(), any()) } just runs
        viewModel.identityPoolId = IDENTITY_POOL_ID
        viewModel.mapStyle = ""
        viewModel.apiKeyRegion = API_KEY_REGION
        viewModel.apiKey = API_KEY
        viewModel.trackerName = TRACKER_NAME

        assertEquals(true, viewModel.checkValidations(context))
    }

    @Test
    fun `test checkValidations when all fields are not empty`() {
        viewModel.identityPoolId = IDENTITY_POOL_ID
        viewModel.mapStyle = MAP_STYLE
        viewModel.apiKeyRegion = API_KEY_REGION
        viewModel.apiKey = API_KEY
        viewModel.trackerName = TRACKER_NAME

        assertEquals(false, viewModel.checkValidations(context))
    }
}