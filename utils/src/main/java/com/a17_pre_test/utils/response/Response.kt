package com.a17_pre_test.utils.response

sealed class Response<out SuccessParam> {
    data class Success<out SuccessParam>(val data: SuccessParam) : Response<SuccessParam>()
    data class Failure(val failure: com.a17_pre_test.utils.failure.Failure) :
        Response<Nothing>()

    val isSuccess get() = this is Success
    val isFailure get() = this is Failure

    suspend fun <T> then(block: suspend (pass: SuccessParam) -> Response<T>): Response<T> {
        if (this is Success) {
            return block(data)
        }

        if (this is Failure) {
            return Failure(failure)
        }

        throw IllegalStateException("Condition should be exhausted")
    }

    suspend fun catch(block: suspend (failure: com.a17_pre_test.utils.failure.Failure) -> Unit): Response<SuccessParam> {
        if (this is Failure) {
            block(failure)
        }
        return this
    }
}