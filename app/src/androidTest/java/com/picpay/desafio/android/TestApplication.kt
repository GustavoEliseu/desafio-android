package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.modules.provideOkHttpClient
import com.picpay.desafio.android.modules.providePicPayService
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestApplication : Application() {
    private val testModule = module {
        single {
            provideOkHttpClient()
        }

        single{MockWebServer()}

        single {
            val mockWebServer: MockWebServer = get()
            Retrofit.Builder()
                .baseUrl("https://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        single {
            providePicPayService(get())
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(testModule)
        }
    }
}