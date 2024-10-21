package com.picpay.desafio.android.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.domain.model.User

class UserListAdapter() : RecyclerView.Adapter<UserListItemViewHolder>() {

    private var _users = emptyList<User>()

    // Update users and notify the adapter of the changes
    fun setUsers(newUsers: List<User>) {
        val diffResult = DiffUtil.calculateDiff(UserListDiffCallback(_users, newUsers))
        _users = newUsers
        diffResult.dispatchUpdatesTo(this)
    }


    fun getUsers(): List<User>{
        return _users
    }

    fun isEmpty():Boolean{
        return _users.isEmpty()
    }

    fun isNotEmpty():Boolean{
        return _users.isNotEmpty()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val databinding = DataBindingUtil.inflate<ListItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user,
            parent,
            false
        )
        return UserListItemViewHolder(databinding)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(_users[position])
    }

    override fun getItemCount(): Int = _users.size
}