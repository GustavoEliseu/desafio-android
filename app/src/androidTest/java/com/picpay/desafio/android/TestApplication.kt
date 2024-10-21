package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.modules.networkModule
import com.picpay.desafio.android.di.modules.useCaseModule
import com.picpay.desafio.android.di.modules.viewModelModule
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(networkModule, viewModelModule, useCaseModule)
        }
    }
}