package com.lihan.qrcraft.scan.di

import com.lihan.qrcraft.scan.data.repository.QRCodeScanRepository
import com.lihan.qrcraft.scan.domain.repository.ScanRepository
import com.lihan.qrcraft.scan.presentation.ScanViewModel
import com.lihan.qrcraft.scan.presentation.result.ScanResultViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scanModule = module {

    singleOf(::QRCodeScanRepository).bind<ScanRepository>()

    viewModelOf(::ScanViewModel)
    viewModelOf(::ScanResultViewModel)
}