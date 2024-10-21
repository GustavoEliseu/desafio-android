package com.picpay.desafio.android.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.picpay.desafio.android.domain.model.User

class UserListDiffCallback(
    private val oldList: List<User>?,
    private val newList: List<User>?
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem?.username == newItem?.username
    }

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size  ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem == newItem
    }
}