package com.picpay.desafio.android.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.ui.main.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by inject()
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is MainViewModel.MainViewState.Loading -> viewModel.setLoadingState()
                is MainViewModel.MainViewState.Success -> viewModel.updateUsers(state.data)
                is MainViewModel.MainViewState.Error -> {
                    Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
                    viewModel.updateRequestError()
                }
                else -> viewModel.updateUsers()
            }
        }
        viewModel.initialize()
    }
}
