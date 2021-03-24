package com.a17_pre_test.data

import com.a17_pre_test.data.data_source.RemoteDataSourceImp
import com.a17_pre_test.data.retrofit.services.GithubUserService
import com.a17_pre_test.domain.data_source.RemoteDataSource
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single<GithubUserService> { get<Retrofit>().create(GithubUserService::class.java) }

    single<RemoteDataSource> { RemoteDataSourceImp(get()) }
}