package com.picpay.desafio.android

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Response

class ExampleServiceTest {

    private val api = mock<PicPayService>()

    private val service = ExampleService(api)

    @Test
    fun exampleTest() {
        // given
        val call = mock<Call<List<User>>>()
        val expectedUsers = emptyList<User>()

        `when`(call.execute()).thenReturn(Response.success(expectedUsers))
        `when`(api.getUsers()).thenReturn(call)

        // when
        val users = service.example()

        // then
        assertEquals(users, expectedUsers)
    }
}