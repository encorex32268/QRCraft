package com.lihan.qrcraft.core.presentation.screens.preview

sealed interface PreviewAction{
    data object BackClick: PreviewAction
    data object CopyClick: PreviewAction
    data object ShareClick: PreviewAction
    data object SaveClick: PreviewAction
}