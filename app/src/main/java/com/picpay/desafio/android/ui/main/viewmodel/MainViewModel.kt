package com.picpay.desafio.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUserListUseCase
import com.picpay.desafio.android.ui.main.adapter.UserListAdapter
import kotlinx.coroutines.launch

class MainViewModel(private val getUserListUseCase: GetUserListUseCase): ViewModel() {
    val adapter:UserListAdapter = UserListAdapter()
    val mutableRecyclerVisibility = MutableLiveData<Boolean>()
    val mutableMessageVisibility = MutableLiveData<Boolean>()
    val mutableLoadingVisibility = MutableLiveData<Boolean>()
    private val _viewState = MutableLiveData<MainViewState<List<User>>>()
    val viewState: LiveData<MainViewState<List<User>>> get() = _viewState

    fun initialize(){
        _viewState.value = MainViewState.Loading
        viewModelScope.launch {
            getUsers()
        }
    }

    fun setLoadingState(){
        setLoadingVisibility(true)
    }

    fun setRecreateState(){
        _viewState.value = MainViewState.Recreated
    }

    fun updateUsers(users:List<User> = listOf(), recreated:Boolean = false){
        if(!recreated) adapter.setUsers(users)
        setLoadingVisibility(false)
        setRecyclerViewVisibility(adapter.isNotEmpty())
        setMessageVisibility(adapter.isEmpty())
    }

    fun updateRequestError(){
        setLoadingVisibility(false)
        setRecyclerViewVisibility(false)
        setMessageVisibility(true)
    }

    private fun getUsers(){
        viewModelScope.launch {
            val result = getUserListUseCase.execute()
            result.onSuccess { users ->
                _viewState.value = if (users.isEmpty()) MainViewState.Empty else MainViewState.Success(users)
            }.onFailure { error ->
                _viewState.value = MainViewState.Error(error.localizedMessage ?: error.message ?: "")
            }
        }
    }

    private fun setRecyclerViewVisibility(visible: Boolean){
        mutableRecyclerVisibility.value = visible
    }

    private fun setMessageVisibility(visible: Boolean){
        mutableMessageVisibility.value = visible
    }

    private fun setLoadingVisibility(visible: Boolean){
        mutableLoadingVisibility.value = visible
    }

    sealed class MainViewState<out T> {
        data object Recreated : MainViewState<Nothing>()
        data object Loading : MainViewState<Nothing>()
        data class Success<out T>(val data: T) : MainViewState<T>()
        data class Error(val message: String?) : MainViewState<Nothing>()
        data object Empty : MainViewState<Nothing>()
    }
}