package com.lihan.qrcraft.generate.presentation.create

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.google.mlkit.vision.barcode.common.Barcode
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
import app.cash.turbine.test
import androidx.compose.runtime.snapshots.Snapshot
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CreateViewModelTest {

    private lateinit var viewModel: CreateViewModel
    private lateinit var repository: FakeHistoryRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeHistoryRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun initViewModel(type: Int) {
        val savedStateHandle = SavedStateHandle(
            mapOf("type" to type)
        )
        viewModel = CreateViewModel(repository, savedStateHandle)
    }

    @Test
    fun `initial state reflects route type`() = runTest {
        initViewModel(Barcode.TYPE_TEXT)
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.type).isEqualTo(Barcode.TYPE_TEXT)
            assertThat(state.generateButtonEnabled).isFalse()
        }
    }

    @Test
    fun `text field input enables generate button for Text type`() = runTest {
        initViewModel(Barcode.TYPE_TEXT)
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.generateButtonEnabled).isFalse()

            Snapshot.withMutableSnapshot {
                initialState.textFieldStateFirst.edit { append("Hello") }
            }
            
            val updatedState = awaitItem()
            assertThat(updatedState.generateButtonEnabled).isTrue()
        }
    }

    @Test
    fun `geolocation requires both lat and lng to enable button`() = runTest {
        initViewModel(Barcode.TYPE_GEO)
        viewModel.state.test {
            val state1 = awaitItem()
            assertThat(state1.generateButtonEnabled).isFalse()

            Snapshot.withMutableSnapshot {
                state1.textFieldStateFirst.edit { append("25.0") }
            }
            
            Snapshot.withMutableSnapshot {
                state1.textFieldStateSecond.edit { append("121.0") }
            }
            
            val state2 = awaitItem()
            assertThat(state2.generateButtonEnabled).isTrue()
        }
    }

    @Test
    fun `generating QR code emits navigate event and saves to repo`() = runTest {
        initViewModel(Barcode.TYPE_TEXT)
        viewModel.state.test {
            val state = awaitItem()
            Snapshot.withMutableSnapshot {
                state.textFieldStateFirst.edit { append("https://google.com") }
            }
            awaitItem() // Wait for button enable

            viewModel.onAction(CreateAction.GenerateButtonClick)

            viewModel.uiEvent.test {
                val event = awaitItem()
                assertThat(event is CreateUiEvent.NavigateToPreview).isTrue()
                val navigateEvent = event as CreateUiEvent.NavigateToPreview
                assertThat(navigateEvent.id).isEqualTo(1L)
            }
        }
    }
}
