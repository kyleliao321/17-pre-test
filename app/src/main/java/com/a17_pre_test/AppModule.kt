package com.a17_pre_test

import com.a17_pre_test.data.dataModule
import com.a17_pre_test.domain.domainModule
import com.a17_pre_test.presentation.presentationModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

internal val AppModule = module {
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag("HTTP").d(it)
        }).apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    loadKoinModules(dataModule)
    loadKoinModules(domainModule)
    loadKoinModules(presentationModule)
}