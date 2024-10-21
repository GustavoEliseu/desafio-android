package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.modules.networkModule
import com.picpay.desafio.android.di.modules.useCaseModule
import com.picpay.desafio.android.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication  : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            properties(mapOf("baseUrl" to "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"))
            modules(networkModule, viewModelModule, useCaseModule)
        }
    }
}