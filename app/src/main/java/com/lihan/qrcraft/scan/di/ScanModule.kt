package com.lihan.qrcraft.scan.di

import com.lihan.qrcraft.scan.data.AndroidClipboard
import com.lihan.qrcraft.scan.domain.Clipboard
import com.lihan.qrcraft.scan.presentation.ScanViewModel
import com.lihan.qrcraft.scan.presentation.result.ScanResultViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scanModule = module {

    singleOf(::AndroidClipboard).bind<Clipboard>()

    viewModelOf(::ScanViewModel)
    viewModelOf(::ScanResultViewModel)
}