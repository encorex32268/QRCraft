package com.lihan.qrcraft

import android.app.Application
import com.lihan.qrcraft.core.di.coreModule
import com.lihan.qrcraft.generate.di.generateModule
import com.lihan.qrcraft.scan.di.scanModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class QRCraftApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.DEBUG)
            androidContext(this@QRCraftApp)
            modules(
                listOf(
                    coreModule,
                    scanModule,
                    generateModule
                )
            )
        }
    }
}