package com.picpay.desafio.android.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.databinding.RecycleTitleHeaderBinding

class HeaderViewHolder(
private val binding: RecycleTitleHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(title:String){
        binding.title.text = title
    }
}