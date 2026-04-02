package com.lihan.qrcraft.core.di

import androidx.room.Room
import com.lihan.qrcraft.core.data.AndroidClipboard
import com.lihan.qrcraft.core.data.local.QRCodeHistoryDao
import com.lihan.qrcraft.core.data.local.QRCraftDatabase
import com.lihan.qrcraft.core.data.repository.QRCodeHistoryRepository
import com.lihan.qrcraft.core.domain.Clipboard
import com.lihan.qrcraft.core.domain.repository.HistoryRepository
import com.lihan.qrcraft.core.presentation.screens.preview.PreviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        Room
            .databaseBuilder(
                androidContext(),
                QRCraftDatabase::class.java,
                "qrcraft.db"
            )
            .build()
    }

    single { get<QRCraftDatabase>().qrCodeHistoryDao}.bind<QRCodeHistoryDao>()

    singleOf(::AndroidClipboard).bind<Clipboard>()
    singleOf(::QRCodeHistoryRepository).bind<HistoryRepository>()

    viewModelOf(::PreviewViewModel)

}