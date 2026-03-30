package com.lihan.qrcraft.scan.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScanViewModel: ViewModel() {

    private val _state = MutableStateFlow(ScanState())
    val state = _state.asStateFlow()

    fun onAction(action: ScanAction){
        when(action){
            ScanAction.CloseAppClick -> Unit
            ScanAction.GrantAccessClick -> Unit
            ScanAction.DismissCameraPermissionDialog -> {
                _state.update { it.copy(
                    isShowCameraPermissionDialog = false
                ) }
            }
            ScanAction.ShowCameraPermissionDialog ->{
                _state.update { it.copy(
                    isShowCameraPermissionDialog = true
                ) }
            }
        }
    }
}