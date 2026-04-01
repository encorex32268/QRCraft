package com.lihan.qrcraft.generate.presentation.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.lihan.qrcraft.core.domain.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val route = savedStateHandle.toRoute<Route.Create>()

    private val _state = MutableStateFlow(
        CreateState(
            type = route.type
        )
    )
    val state = _state.asStateFlow()

    fun onAction(action: CreateAction){
        when(action){
            CreateAction.BackClick -> Unit
            CreateAction.GenerateButtonClick -> generateQRCode()
        }
    }

    private fun generateQRCode() {

    }
}