package com.picpay.desafio.android.di.modules

import com.picpay.desafio.android.ui.main.viewmodel.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get())}
}