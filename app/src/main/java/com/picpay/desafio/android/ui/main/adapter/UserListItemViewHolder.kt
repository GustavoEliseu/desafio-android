package com.picpay.desafio.android.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.ui.main.viewmodel.UserListItemViewModel
import com.picpay.desafio.android.model.User
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListItemViewHolder(
    private val binding: ListItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = UserListItemViewModel()


    fun bind(user: User) {
        binding.viewModel = viewModel
        viewModel.initialize(user)
        viewModel.progressVisible(true)
        Picasso.get()
            .load(user.img)
            .placeholder(R.drawable.ic_round_account_circle)
            .error(R.drawable.ic_round_account_circle)
            .into(binding.picture, object : Callback {
                override fun onSuccess() {
                    viewModel.progressVisible(false)
                    binding.executePendingBindings()
                }

                override fun onError(e: Exception?) {
                    viewModel.progressVisible(false)
                    binding.executePendingBindings()
                }
            })
    }
}