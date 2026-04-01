package com.lihan.qrcraft.generate.presentation.create

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.Route
import io.github.alexzhirkevich.qrose.QrData
import io.github.alexzhirkevich.qrose.location
import io.github.alexzhirkevich.qrose.phone
import io.github.alexzhirkevich.qrose.text
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.Create>()

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<CreateUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(
        CreateState(type = route.type)
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeTextField()
                hasLoadedInitialData = true
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateState(type = route.type)
        )

    fun onAction(action: CreateAction) {
        when (action) {
            CreateAction.BackClick -> Unit
            CreateAction.GenerateButtonClick -> generateQRCode()
        }
    }


    private fun generateQRCode(){
        val currentState= state.value

        if (currentState.type == null){
            return
        }

        val type = QRCodeType.getQRCodeType(currentState.type)

        val firstTextFieldString = currentState.textFieldStateFirst.text.toString()
        val secondTextFieldString = currentState.textFieldStateSecond.text.toString()
        val thirdTextFieldString = currentState.textFieldStateThird.text.toString()

        val dataString = when(type){
            QRCodeType.PhoneNumber -> QrData.phone(firstTextFieldString)
            QRCodeType.Link,
            QRCodeType.Text -> QrData.text(firstTextFieldString)
            QRCodeType.Geolocation -> {
                val lat = firstTextFieldString.toFloatOrNull()?:0f
                val lng = secondTextFieldString.toFloatOrNull()?:0f
                QrData.text("${lat},${lng}")
            }
            QRCodeType.Contact,
            QRCodeType.WiFi -> {
                QrData.text(
                    firstTextFieldString + "\n" + secondTextFieldString + "\n" + thirdTextFieldString
                )
            }

        }

        viewModelScope.launch {
            _uiEvent.send(
                CreateUiEvent.NavigateToPreview(
                    type = currentState.type,
                    dataString = dataString
                )
            )
        }
    }


    private fun observeTextField() {

        val type = QRCodeType.getQRCodeType(state.value.type)
        when (type) {
            QRCodeType.Text,
            QRCodeType.Link,
            QRCodeType.PhoneNumber -> {
                snapshotFlow {
                    state.value.textFieldStateFirst.text.toString()
                }.onEach { text ->
                    _state.update {
                        it.copy(
                            generateButtonEnabled = text.isNotEmpty()
                        )
                    }
                }.launchIn(viewModelScope)
            }

            QRCodeType.Geolocation -> {
                combine(
                    flow = snapshotFlow { state.value.textFieldStateFirst.text.toString() },
                    flow2 = snapshotFlow { state.value.textFieldStateSecond.text.toString() }
                ) { firstText, secondText ->
                    _state.update {
                        it.copy(
                            generateButtonEnabled = firstText.isNotEmpty() && secondText.isNotEmpty()
                        )
                    }
                }.launchIn(viewModelScope)
            }

            QRCodeType.Contact,
            QRCodeType.WiFi -> {
                combine(
                    flow = snapshotFlow { state.value.textFieldStateFirst.text.toString() },
                    flow2 = snapshotFlow { state.value.textFieldStateSecond.text.toString() },
                    flow3 =snapshotFlow { state.value.textFieldStateThird.text.toString() },
                ) { firstText, secondText, thirdText ->
                    _state.update {
                        it.copy(
                            generateButtonEnabled = firstText.isNotEmpty() &&
                                    secondText.isNotEmpty() &&
                                    thirdText.isNotEmpty()
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }

    }


}