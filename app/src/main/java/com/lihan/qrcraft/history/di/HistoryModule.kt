package com.lihan.qrcraft.history.di

import com.lihan.qrcraft.history.presentation.ScanHistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {

    viewModelOf(::ScanHistoryViewModel)
}