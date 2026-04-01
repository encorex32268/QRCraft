package com.lihan.qrcraft.scan.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.Clipboard
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.scan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanResultViewModel(
    private val repository: ScanRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val route = savedStateHandle.toRoute<Route.ScanResult>()

    private val _state = MutableStateFlow(
        ScanResultState(
            type = route.type,
            content = route.content
        )
    )
    val state = _state.asStateFlow()


    fun onAction(action: ScanResultAction){
        when(action){
            ScanResultAction.CopyClick -> copyToClipboard()
            else -> Unit
        }
    }

    private fun copyToClipboard() {
        repository.copyText(state.value.content)
    }
}