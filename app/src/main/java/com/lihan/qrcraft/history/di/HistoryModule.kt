package com.lihan.qrcraft.history.di

import com.lihan.qrcraft.history.data.ScanHistoryRepository
import com.lihan.qrcraft.history.domain.HistoryRepository
import com.lihan.qrcraft.history.presentation.ScanHistoryViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val historyModule = module {

    singleOf(::ScanHistoryRepository).bind<HistoryRepository>()

    viewModelOf(::ScanHistoryViewModel)
}