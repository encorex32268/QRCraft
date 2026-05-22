package com.lihan.qrcraft.core.presentation.screens.preview

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.google.mlkit.vision.barcode.common.Barcode
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.core.domain.repository.FakeClipboard
import com.lihan.qrcraft.core.domain.repository.FakeFileManager
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PreviewViewModelTest {

    private lateinit var viewModel: PreviewViewModel
    private lateinit var repository: FakeHistoryRepository
    private lateinit var clipboard: FakeClipboard
    private lateinit var fileManager: FakeFileManager
    private val testDispatcher = UnconfinedTestDispatcher()

    private val testId = 1L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeHistoryRepository()
        clipboard = FakeClipboard()
        fileManager = FakeFileManager()
        
        runTest {
            repository.upsert(
                QRCodeHistory(
                    id = testId,
                    type = Barcode.TYPE_TEXT,
                    content = "Test content",
                    createdAt = Instant.now().toEpochMilli(),
                    isGenerated = true,
                    isFavorite = false,
                    title = "Initial Title"
                )
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initViewModel(id: Long = testId, screenTitle: String = "Preview") {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "id" to id,
                "screenTitle" to screenTitle
            )
        )
        viewModel = PreviewViewModel(clipboard, savedStateHandle, repository, fileManager)
    }

    @Test
    fun `initial state loads data from repository`() = runTest {
        initViewModel()
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.qrCodeHistoryUi).isNotNull()
            assertThat(state.qrCodeHistoryUi?.id).isEqualTo(testId)
            assertThat(state.qrCodeHistoryUi?.content).isEqualTo("Test content")
        }
    }

    @Test
    fun `copy action copies content to clipboard`() = runTest {
        initViewModel()
        viewModel.state.test {
            awaitItem()
            viewModel.onAction(PreviewAction.CopyClick)
            assertThat(clipboard.copiedText).isEqualTo("Test content")
        }
    }

    @Test
    fun `favorite action updates status`() = runTest {
        initViewModel()
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.qrCodeHistoryUi?.isFavorite).isEqualTo(false)

            viewModel.onAction(PreviewAction.FavoriteClick)
            val updatedState = awaitItem()
            assertThat(updatedState.qrCodeHistoryUi?.isFavorite).isEqualTo(true)
        }
    }

    @Test
    fun `share action sends UI event`() = runTest {
        initViewModel()
        viewModel.state.test {
            awaitItem()
            viewModel.uiEvent.test {
                viewModel.onAction(PreviewAction.ShareClick)
                val event = awaitItem()
                assertThat(event is PreviewUiEvent.ShareQRCode).isTrue()
                val shareEvent = event as PreviewUiEvent.ShareQRCode
                assertThat(shareEvent.content).isEqualTo("Test content")
            }
        }
    }

    @Test
    fun `back click sends back event`() = runTest {
        initViewModel()
        viewModel.state.test {
            awaitItem()
            viewModel.uiEvent.test {
                viewModel.onAction(PreviewAction.BackClick)
                val event = awaitItem()
                assertThat(event is PreviewUiEvent.Back).isTrue()
            }
        }
    }
}
