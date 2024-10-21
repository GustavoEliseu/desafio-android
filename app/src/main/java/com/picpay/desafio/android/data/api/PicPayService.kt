package com.picpay.desafio.android.data.api

import com.picpay.desafio.android.domain.model.User
import retrofit2.Response
import retrofit2.http.GET


interface PicPayService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}