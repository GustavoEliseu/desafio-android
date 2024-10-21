package com.picpay.desafio.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUserListUseCase
import com.picpay.desafio.android.ui.main.viewmodel.MainViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getUserListUseCase: GetUserListUseCase

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUserListUseCase = mock()
        mainViewModel = MainViewModel(getUserListUseCase)
    }

    @After
    fun reset(){
        Dispatchers.resetMain()
    }

    @Test
    fun `initialize should set loading state and get users`() = runTest{
        val mockUsers = listOf(User(id = 1, name = "Test User", username = "testuser", img = ""))
        `when`(getUserListUseCase.execute()).thenReturn(Result.success(mockUsers))
        mainViewModel.initialize()

        assertEquals(MainViewModel.MainViewState.Loading, mainViewModel.viewState.value)
        advanceUntilIdle()
        assertEquals(MainViewModel.MainViewState.Success(mockUsers), mainViewModel.viewState.value)
    }

    @Test
    fun `initialize should set error state when user retrieval fails`() = runTest {
        val errorMessage = "Network error"
        `when`(getUserListUseCase.execute()).thenReturn(Result.failure(Exception(errorMessage)))

        mainViewModel.initialize()

        assertEquals(MainViewModel.MainViewState.Loading, mainViewModel.viewState.value)
        advanceUntilIdle()
        assertTrue(mainViewModel.viewState.value is MainViewModel.MainViewState.Error)
        assertEquals(errorMessage, (mainViewModel.viewState.value as MainViewModel.MainViewState.Error).message)
    }

    @Test
    fun `updateUsers should set adapter users and visibility states`() = runTest {
        val mockUsers = listOf(User(id = 1, name = "Test User", username = "testuser", img = ""))
        mainViewModel.updateUsers(mockUsers)

        advanceUntilIdle()
        assertEquals(mockUsers, mainViewModel.adapter.getUsers())
        assertEquals(false, mainViewModel.mutableLoadingVisibility.value)
        assertEquals(true, mainViewModel.mutableRecyclerVisibility.value)
        assertEquals(false, mainViewModel.mutableMessageVisibility.value)
    }

}