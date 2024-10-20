package com.picpay.desafio.android.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.desafio.android.model.User

class UserListItemViewModel:
    ViewModel() {

        val mutableUserName = MutableLiveData<String>()
        val mutableName = MutableLiveData<String>()
        val mutableProgressVisibility = MutableLiveData<Boolean>(false)

        fun initialize(user: User){
            mutableUserName.value= user.username ?: ""
            mutableName.value= user.name ?: ""
        }

        fun progressVisible(isVisible: Boolean){
            mutableProgressVisibility.value = isVisible
        }
}