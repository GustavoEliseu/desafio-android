package com.picpay.desafio.android.di

import com.picpay.desafio.android.PicPayService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), getProperty("baseUrl")) }
    single { providePicPayService(get()) }
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String):Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun providePicPayService(retrofit: Retrofit): PicPayService {
    return retrofit.create(PicPayService::class.java)
}