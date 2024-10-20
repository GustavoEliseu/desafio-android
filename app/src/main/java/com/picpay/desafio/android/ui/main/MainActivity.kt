package com.picpay.desafio.android.ui.main

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SavedState
import com.google.android.material.appbar.AppBarLayout
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.ui.main.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by inject()
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        /*Ao utilizar versões recentes do recyclerView, não é necessário lidar com savedInstanceState
            apenas remover o nestedScroll que atrapalhava e adicionar chaves ao adapter(ver no viewmodel)
         */
        super.onCreate(savedInstanceState)
        viewModel.adapter.setNewTitle(getString(R.string.title))
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        if (savedInstanceState != null) {
            viewModel.setRecreateState()
        }else{
            viewModel.initialize()
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.viewState.observe(this) { state ->
            when (state) {
                is MainViewModel.MainViewState.Loading -> { viewModel.setLoadingState()}
                is MainViewModel.MainViewState.Success -> viewModel.updateUsers(state.data)
                is MainViewModel.MainViewState.Error -> {
                    Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
                    viewModel.updateRequestError()
                }
                is MainViewModel.MainViewState.Empty ->{ viewModel.updateUsers()}
                else -> {
                    viewModel.updateUsers(recreated = true)
                }
            }
        }
    }
}
