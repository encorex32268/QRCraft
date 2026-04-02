package com.lihan.qrcraft.scan.di

import com.lihan.qrcraft.scan.domain.QRCodeImageConverter
import com.lihan.qrcraft.scan.presentation.ScanViewModel
import com.lihan.qrcraft.scan.presentation.util.QRCodeImageConverterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scanModule = module {

    singleOf(::QRCodeImageConverterImpl).bind<QRCodeImageConverter>()

    viewModelOf(::ScanViewModel)
}