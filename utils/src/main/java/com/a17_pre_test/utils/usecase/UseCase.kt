package com.a17_pre_test.utils.usecase

import com.a17_pre_test.utils.response.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<out Return, in Params>(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    abstract suspend fun run(params: Params): Response<Return>

    suspend operator fun invoke(params: Params): Response<Return> =
        withContext(defaultDispatcher) {
            run(params)
        }
}