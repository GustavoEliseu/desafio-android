package com.picpay.desafio.android.modules

import com.picpay.desafio.android.data.api.PicPayService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(HeaderInterceptor())
        .build()
}

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Cache-Control", "no-cache")
                .build()
        )
    }
}
var mBaseUrl = "https://www.teste.com.br/"

fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String? = null):Retrofit {
    if(baseUrl!= mBaseUrl && baseUrl!= null) mBaseUrl=baseUrl
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(mBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun providePicPayService(retrofit: Retrofit): PicPayService {
    return retrofit.create(PicPayService::class.java)
}