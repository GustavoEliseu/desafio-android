package com.picpay.desafio.android.ui.main.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.ui.main.adapter.UserListAdapter
import com.picpay.desafio.android.PicPayService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val service: PicPayService): ViewModel() {
    val adapter = UserListAdapter()
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

    fun updateUsers(users:List<User> = listOf()){
        adapter.users = users
        setLoadingVisibility(false)
        setRecyclerViewVisibility(users.isNotEmpty())
        setMessageVisibility(users.isEmpty())
    }

    fun updateRequestError(){
        setLoadingVisibility(false)
        setRecyclerViewVisibility(false)
        setMessageVisibility(true)
    }

    private fun getUsers(){
        service.getUsers()
            .enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _viewState.value = MainViewState.Error(t.localizedMessage ?: t.message ?: "")
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    val code = response.code()
                    if(code == 200) {
                        val body = response.body()
                        _viewState.value = if(body.isNullOrEmpty()) MainViewState.Empty else MainViewState.Success(body)
                        if(body == null){
                            _viewState.value = MainViewState.Success(listOf())
                        }else{
                            _viewState.value = MainViewState.Success(body)
                        }
                    } else{
                        _viewState.value = MainViewState.Empty
                    }
                }
            })
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
        object Loading : MainViewState<Nothing>()
        data class Success<out T>(val data: T) : MainViewState<T>()
        data class Error(val message: String?) : MainViewState<Nothing>()
        object Empty : MainViewState<Nothing>()
    }
}