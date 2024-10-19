package com.picpay.desafio.android.di

import com.picpay.desafio.android.ui.main.viewmodel.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get()) }
}