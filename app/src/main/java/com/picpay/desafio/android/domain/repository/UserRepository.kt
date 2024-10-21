package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(): Response<List<User>>
}