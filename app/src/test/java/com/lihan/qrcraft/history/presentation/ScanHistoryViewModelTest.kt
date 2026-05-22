package com.lihan.qrcraft.history.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.google.mlkit.vision.barcode.common.Barcode
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.core.domain.repository.FakeHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class ScanHistoryViewModelTest {

    private lateinit var viewModel: ScanHistoryViewModel
    private lateinit var repository: FakeHistoryRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeHistoryRepository()
        viewModel = ScanHistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observing histories updates state`() = runTest {
        val scannedHistory = QRCodeHistory(
            id = 1L,
            type = Barcode.TYPE_TEXT,
            content = "Scanned",
            createdAt = Instant.now().toEpochMilli(),
            isGenerated = false,
            isFavorite = false
        )
        val generatedHistory = QRCodeHistory(
            id = 2L,
            type = Barcode.TYPE_URL,
            content = "Generated",
            createdAt = Instant.now().toEpochMilli(),
            isGenerated = true,
            isFavorite = true
        )

        repository.upsert(scannedHistory)
        repository.upsert(generatedHistory)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.scannedItems.size).isEqualTo(1)
            assertThat(state.generatedItems.size).isEqualTo(1)
            assertThat(state.scannedItems[0].content).isEqualTo("Scanned")
            assertThat(state.generatedItems[0].content).isEqualTo("Generated")
        }
    }

    @Test
    fun `long click shows editor bottom sheet`() = runTest {
        viewModel.state.test {
            awaitItem() // initial state
            viewModel.onAction(ScanHistoryAction.ItemLongClick(1L))
            val state = awaitItem()
            assertThat(state.isShowEditorBottomSheet).isTrue()
            assertThat(state.selectedId).isEqualTo(1L)
        }
    }

    @Test
    fun `delete item removes it from repository and hides bottom sheet`() = runTest {
        val history = QRCodeHistory(
            id = 1L,
            type = Barcode.TYPE_TEXT,
            content = "To be deleted",
            createdAt = Instant.now().toEpochMilli(),
            isGenerated = false,
            isFavorite = false
        )
        repository.upsert(history)

        viewModel.state.test {
            awaitItem() // initial state with 1 item
            viewModel.onAction(ScanHistoryAction.ItemLongClick(1L))
            awaitItem() // bottom sheet shown

            viewModel.onAction(ScanHistoryAction.DeleteClick)
            val stateAfterDelete = awaitItem()
            assertThat(stateAfterDelete.isShowEditorBottomSheet).isFalse()
            assertThat(stateAfterDelete.selectedId).isEqualTo(null)
            assertThat(stateAfterDelete.scannedItems.isEmpty()).isTrue()
        }
    }

    @Test
    fun `item favorite click updates status in repository`() = runTest {
        val history = QRCodeHistory(
            id = 1L,
            type = Barcode.TYPE_TEXT,
            content = "Favorite test",
            createdAt = Instant.now().toEpochMilli(),
            isGenerated = false,
            isFavorite = false
        )
        repository.upsert(history)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.scannedItems[0].isFavorite).isFalse()

            viewModel.onAction(ScanHistoryAction.ItemFavoriteClick(1L, false))
            val updatedState = awaitItem()
            assertThat(updatedState.scannedItems[0].isFavorite).isTrue()
        }
    }

    @Test
    fun `share action sends UI event`() = runTest {
        val history = QRCodeHistory(
            id = 1L,
            type = Barcode.TYPE_TEXT,
            content = "Shared content",
            createdAt = Instant.now().toEpochMilli(),
            isGenerated = false,
            isFavorite = false
        )
        repository.upsert(history)

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ScanHistoryAction.ItemLongClick(1L))
            awaitItem()

            viewModel.uiEvent.test {
                viewModel.onAction(ScanHistoryAction.ShareClick)
                val event = awaitItem()
                assertThat(event is ScanHistoryUiEvent.ShareQRCode).isTrue()
                val shareEvent = event as ScanHistoryUiEvent.ShareQRCode
                assertThat(shareEvent.content).isEqualTo("Shared content")
            }
        }
    }
}
