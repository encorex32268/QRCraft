package com.lihan.qrcraft.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.repository.HistoryRepository
import com.lihan.qrcraft.core.presentation.mapper.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanHistoryViewModel(
    private val repository: HistoryRepository
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _uiEvent = Channel<ScanHistoryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(ScanHistoryState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData){
                observeHistories()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ScanHistoryState()
        )


    fun onAction(action: ScanHistoryAction){
        when(action){
            is ScanHistoryAction.ItemLongClick -> itemLongClick(action.id)
            ScanHistoryAction.DeleteClick -> deleteItem()
            ScanHistoryAction.ShareClick -> shareQRCode()
            is ScanHistoryAction.ItemClick -> itemClick(action.id)
            ScanHistoryAction.DismissEditorBottomSheet -> {
                _state.update { it.copy(
                    isShowEditorBottomSheet = false,
                    selectedId = null
                ) }
            }

        }
    }

    private fun shareQRCode(){
        val currentState = state.value
        val selectId = currentState.selectedId ?: return

        val totalHistory = currentState.scannedItems + currentState.generatedItems
        val findItem = totalHistory.find { it.id ==  selectId}

        if (findItem == null){
            _state.update { it.copy(
                isShowEditorBottomSheet = false,
                selectedId = null
            ) }
            return
        }

        viewModelScope.launch {
            val type = QRCodeType.getQRCodeType(findItem.type)

            _uiEvent.send(
                ScanHistoryUiEvent.ShareQRCode(
                    title = findItem.title?:type.name,
                    content = findItem.content
                )
            )
        }

    }

    private fun deleteItem(){
        val currentSelectedId = state.value.selectedId ?: return

        viewModelScope.launch {
            repository.deleteHistory(currentSelectedId)

            _state.update { it.copy(
                isShowEditorBottomSheet = false,
                selectedId = null
            ) }
        }


    }

    private fun itemLongClick(id: Long?){
        if (id == null) return
        _state.update { it.copy(
            isShowEditorBottomSheet = true,
            selectedId = id
        ) }
    }

    private fun itemClick(id: Long?){
        if (id == null) return
        viewModelScope.launch {
            _uiEvent.send(
                ScanHistoryUiEvent.NavigateToPreview(id = id)
            )
        }
    }


    private fun observeHistories() {
        repository
            .getScannedHistories()
            .onEach { scannedHistories ->
                _state.update { it.copy(
                    scannedItems = scannedHistories.mapNotNull { domain ->
                        domain.toUi()
                    }
                ) }
            }.launchIn(viewModelScope)

        repository
            .getGeneratedHistories()
            .onEach { generatedHistories ->
                _state.update { it.copy(
                    generatedItems = generatedHistories.mapNotNull { domain ->
                        domain.toUi()
                    }
                ) }
            }.launchIn(viewModelScope)
    }

}