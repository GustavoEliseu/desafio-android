package com.picpay.desafio.android.di.modules

import com.picpay.desafio.android.domain.usecase.GetUserListUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetUserListUseCase(get()) }
}