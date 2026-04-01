package com.lihan.qrcraft.generate.di

import com.lihan.qrcraft.generate.presentation.create.CreateViewModel
import com.lihan.qrcraft.generate.presentation.create.preview.PreviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val generateModule = module {
    viewModelOf(::CreateViewModel)
    viewModelOf(::PreviewViewModel)
}