package com.lihan.qrcraft.generate.presentation.create.preview

sealed interface PreviewAction{
    data object BackClick: PreviewAction
    data object CopyClick: PreviewAction
    data object ShareClick: PreviewAction
}