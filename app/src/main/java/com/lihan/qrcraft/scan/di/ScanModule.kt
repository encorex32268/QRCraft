package com.lihan.qrcraft.scan.di

import com.lihan.qrcraft.scan.presentation.ScanViewModel
import com.lihan.qrcraft.scan.presentation.result.ScanResultViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val scanModule = module {
    viewModelOf(::ScanViewModel)
    viewModelOf(::ScanResultViewModel)
}