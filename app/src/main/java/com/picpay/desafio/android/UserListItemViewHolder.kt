package com.picpay.desafio.android

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.list.UserListItemViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserListItemViewHolder(
    private val binding: ListItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = UserListItemViewModel()

    fun bind(user: User) {
        binding.viewModel = viewModel
        binding.root.findViewById<TextView>(R.id.name).text = user.name
        binding.root.findViewById<TextView>(R.id.username).text = user.username
        binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

        Picasso.get()
            .load(user.img)
            .error(R.drawable.ic_round_account_circle)
            .into(binding.root.findViewById<CircleImageView>(R.id.picture), object : Callback {
                override fun onSuccess() {
                    binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.root.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                }
            })
    }
}