package com.lihan.qrcraft.scan.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.lihan.qrcraft.scan.domain.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanResultViewModel(
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

}