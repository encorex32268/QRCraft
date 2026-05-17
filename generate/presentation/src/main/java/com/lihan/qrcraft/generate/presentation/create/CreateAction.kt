package com.lihan.qrcraft.generate.presentation.create

sealed interface CreateAction {
    data object BackClick: CreateAction
    data object GenerateButtonClick: CreateAction
}