package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserListUseCase(private val userRepository: UserRepository) {

    suspend fun execute(): Result<List<User>>{
        return withContext(Dispatchers.IO) {
            try {
                val response = userRepository.getUsers()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Empty response or failed to fetch users"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}