package com.picpay.desafio.android.di.modules

import android.content.Context
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.util.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

val networkModule = module {
    single { provideOkHttpClient(androidContext()) }
    single { provideRetrofit(get(), getProperty("baseUrl")) }
    single { providePicPayService(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}

private fun provideOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = 5L * 1024 * 1024
    val cacheDir = File(context.cacheDir, "http_cache")
    val cache = Cache(cacheDir, cacheSize)

    return OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (hasNetwork(context))
                request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24).build()
            chain.proceed(request)
        }
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