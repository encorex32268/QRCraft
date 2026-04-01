package com.lihan.qrcraft.generate.presentation.create.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.scan.domain.Clipboard
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.scan.presentation.result.ScanResultAction
import com.lihan.qrcraft.scan.presentation.result.ScanResultState
import io.github.alexzhirkevich.qrose.QrData
import io.github.alexzhirkevich.qrose.enterpriseWifi
import io.github.alexzhirkevich.qrose.location
import io.github.alexzhirkevich.qrose.phone
import io.github.alexzhirkevich.qrose.text
import io.github.alexzhirkevich.qrose.wifi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PreviewViewModel(
    private val clipboard: Clipboard,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val route = savedStateHandle.toRoute<Route.ScanResult>()

    private val _state = MutableStateFlow(
        PreviewState(
            type = route.type,
            content = route.content
        )
    )
    val state = _state.asStateFlow()


    fun onAction(action: PreviewAction){
        when(action){
            PreviewAction.CopyClick -> copyToClipboard()
            else -> Unit
        }
    }

    private fun copyToClipboard() {
        clipboard.copyText(state.value.content)
    }

}













