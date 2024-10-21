package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import retrofit2.Response

class UserRepositoryImpl(private val service: PicPayService): UserRepository {
    override suspend fun getUsers(): Response<List<User>> {
        return service.getUsers()
    }
}