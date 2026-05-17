package com.lihan.qrcraft.core.presentation.di

import com.lihan.qrcraft.core.presentation.screens.preview.PreviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::PreviewViewModel)
}
