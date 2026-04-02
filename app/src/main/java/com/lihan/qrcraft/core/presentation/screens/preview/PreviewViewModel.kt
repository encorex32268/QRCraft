package com.lihan.qrcraft.core.presentation.screens.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.Clipboard
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.domain.repository.HistoryRepository
import com.lihan.qrcraft.core.presentation.mapper.toDomain
import com.lihan.qrcraft.core.presentation.mapper.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val clipboard: Clipboard,
    private val savedStateHandle: SavedStateHandle,
    private val repository: HistoryRepository
): ViewModel(){

    private val route = savedStateHandle.toRoute<Route.Preview>()

    private var originTitle = ""

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<PreviewUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(PreviewState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData){
                setupData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            PreviewState()
        )


    fun onAction(action: PreviewAction){
        when(action){
            PreviewAction.CopyClick -> copyToClipboard()
            PreviewAction.BackClick -> backClick()
            else -> Unit
        }
    }

    private fun setupData(){
        val id = route.id
        viewModelScope.launch {
            val item = repository.getHistoryById(id).first()?.toUi()
            if (item == null){
                _uiEvent.send(PreviewUiEvent.DataError)
                return@launch
            }
            _state.update { it.copy(
                qrCodeHistoryUi = item
            ) }
        }
    }

    private fun copyToClipboard() {
        val currentQRCodeHistoryUi = state.value.qrCodeHistoryUi
        if (currentQRCodeHistoryUi == null){
            viewModelScope.launch {
                _uiEvent.send(PreviewUiEvent.DataError)
            }
        }else{
            clipboard.copyText(currentQRCodeHistoryUi.content)
        }
    }

    private fun backClick(){
        viewModelScope.launch {
            val currentState = state.value
            val currentItem = currentState.qrCodeHistoryUi
            val currentTitle = currentState.title.text.toString()

            if (currentItem == null){
                _uiEvent.send(PreviewUiEvent.DataError)
                return@launch
            }
            if (currentTitle != originTitle){
                repository.upsert(
                    qrCodeHistory = currentItem.copy(
                        title = currentTitle
                    ).toDomain()
                )
            }
            _uiEvent.send(PreviewUiEvent.Back)
        }
    }
}













