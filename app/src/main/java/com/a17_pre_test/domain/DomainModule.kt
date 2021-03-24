package com.a17_pre_test.domain

import com.a17_pre_test.domain.usecases.GetUserByNameFlowUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetUserByNameFlowUseCase(get()) }
}