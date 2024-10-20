package com.picpay.desafio.android.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.UserListDiffCallback
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.databinding.RecycleTitleHeaderBinding
import com.picpay.desafio.android.model.User

class UserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var users = emptyList<User>()
        set(value) {
            val result = DiffUtil.calculateDiff(
                UserListDiffCallback(
                    field,
                    value
                )
            )
            result.dispatchUpdatesTo(this)
            field = value
        }

    private var title:String?= null

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_USER = 1

    fun setNewTitle(newTitle:String?){
        title = newTitle
        notifyItemChanged(0)
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && !title.isNullOrEmpty()) VIEW_TYPE_HEADER else VIEW_TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val databinding = DataBindingUtil.inflate<RecycleTitleHeaderBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recycle_title_header,
            parent,
            false
        )
            HeaderViewHolder(databinding)
        } else {
        val databinding = DataBindingUtil.inflate<ListItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_user,
            parent,
            false
        )
        UserListItemViewHolder(databinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            holder.bind(title ?: "")
        }else if (holder is UserListItemViewHolder){
            holder.bind(if(title.isNullOrEmpty())users[position] else users[position-1])
        }
    }

    override fun getItemCount(): Int = if(title.isNullOrEmpty())users.size else users.size+1
}