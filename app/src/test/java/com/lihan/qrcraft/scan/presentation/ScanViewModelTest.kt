package com.lihan.qrcraft.scan.presentation

import android.net.Uri
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.google.mlkit.vision.barcode.common.Barcode
import com.lihan.qrcraft.core.domain.repository.FakeHistoryRepository
import com.lihan.qrcraft.scan.domain.FakeQRCodeImageConverter
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ScanViewModelTest {

    private lateinit var viewModel: ScanViewModel
    private lateinit var repository: FakeHistoryRepository
    private lateinit var qrCodeImageConverter: FakeQRCodeImageConverter
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeHistoryRepository()
        qrCodeImageConverter = FakeQRCodeImageConverter()
        viewModel = ScanViewModel(repository, qrCodeImageConverter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `flash click toggles flashlight state`() = runTest {
        viewModel.state.test {
            assertThat(awaitItem().isOpeningFlashlight).isFalse()
            viewModel.onAction(ScanAction.FlashClick)
            assertThat(awaitItem().isOpeningFlashlight).isTrue()
        }
    }

    @Test
    fun `scan QR code image success updates state and navigates`() = runTest {
        val uri = mockk<Uri>()
        qrCodeImageConverter.result = Pair(Barcode.TYPE_TEXT, "Image Content")

        viewModel.uiEvent.test {
            viewModel.onAction(ScanAction.ScanQRCodeImage(uri))
            val event = awaitItem()
            assertThat(event is ScanUiEvent.NavigateToPreview).isTrue()
            val navigateEvent = event as ScanUiEvent.NavigateToPreview
            assertThat(navigateEvent.id).isEqualTo(1L)
        }
    }

    @Test
    fun `scan success with barcode saves to repository and navigates`() = runTest {
        val barcode = mockk<Barcode>()
        every { barcode.valueType } returns Barcode.TYPE_TEXT
        every { barcode.rawValue } returns "Barcode Content"

        viewModel.uiEvent.test {
            viewModel.onAction(ScanAction.ScanSuccess(listOf(barcode)))
            
            // The ViewModel has a delay(1000L) in scanSuccess
            // Since we are using UnconfinedTestDispatcher and runTest, 
            // the delay should be skipped or handled.
            
            val event = awaitItem()
            assertThat(event is ScanUiEvent.NavigateToPreview).isTrue()
            assertThat((event as ScanUiEvent.NavigateToPreview).id).isEqualTo(1L)
        }
    }

    @Test
    fun `dismissing permission dialog updates state`() = runTest {
        viewModel.onAction(ScanAction.ShowCameraPermissionDialog)
        assertThat(viewModel.state.value.isShowCameraPermissionDialog).isTrue()

        viewModel.onAction(ScanAction.DismissCameraPermissionDialog)
        assertThat(viewModel.state.value.isShowCameraPermissionDialog).isFalse()
    }
}
