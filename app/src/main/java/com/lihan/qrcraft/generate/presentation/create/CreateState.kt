package com.lihan.qrcraft.generate.presentation.create

import androidx.compose.foundation.text.input.TextFieldState

data class CreateState(
    val type: Int?=null,
    val textFieldStateFirst: TextFieldState = TextFieldState(),
    val textFieldStateSecond: TextFieldState = TextFieldState(),
    val textFieldStateThird: TextFieldState = TextFieldState(),
    val generateButtonEnabled: Boolean = false
)