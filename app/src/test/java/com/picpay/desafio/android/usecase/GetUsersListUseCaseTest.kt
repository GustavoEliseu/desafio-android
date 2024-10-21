package com.picpay.desafio.android.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUserListUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class GetUsersListUseCaseTest {
    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUsersUseCase: GetUserListUseCase

    @Before
    fun setUp() {
        getUsersUseCase = GetUserListUseCase(userRepository)
    }

    @Test
    fun `execute should return list of users when API call is successful`() = runBlocking {
        val mockUserList = listOf(
            User(id = 1, name = "Sandrine Spinka", username = "Tod86", img = ""),
            User(id = 2, name = "Carli Carroll", username = "Constantin_Sawayn", img = "")
        )
        `when`(userRepository.getUsers()).thenReturn(Response.success(mockUserList))

        val result = getUsersUseCase.execute()

        assertTrue(result.isSuccess)
        assertEquals(mockUserList, result.getOrNull())
        assertEquals(mockUserList.size, result.getOrNull()?.size)
    }

    @Test
    fun `execute should return empty list when API response body is null or empty`() = runBlocking {
        `when`(userRepository.getUsers()).thenReturn(Response.success(emptyList()))

        val result = getUsersUseCase.execute()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `execute should return failure when API call fails`() = runBlocking {
        val errorResponse = Response.error<List<User>>(500, ResponseBody.create(null, ""))
        `when`( userRepository.getUsers()).thenReturn(errorResponse)
        val result = getUsersUseCase.execute()
        assertTrue(result.isFailure)
        assertEquals("Empty response or failed to fetch users", result.exceptionOrNull()?.message)
    }

    @Test
    fun `execute should return failure when an exception is thrown`() = runBlocking {
        `when`( userRepository.getUsers()).thenThrow(RuntimeException("Network error"))
        val result = getUsersUseCase.execute()
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}