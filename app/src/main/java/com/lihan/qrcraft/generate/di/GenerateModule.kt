package com.lihan.qrcraft.generate.di

import com.lihan.qrcraft.generate.data.repository.QRCodeGenerateRepository
import com.lihan.qrcraft.generate.domain.repository.GenerateRepository
import com.lihan.qrcraft.generate.presentation.create.CreateViewModel
import com.lihan.qrcraft.generate.presentation.create.preview.PreviewViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val generateModule = module {

    singleOf(::QRCodeGenerateRepository).bind<GenerateRepository>()

    viewModelOf(::CreateViewModel)
    viewModelOf(::PreviewViewModel)
}