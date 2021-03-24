package com.a17_pre_test.utils.failure

sealed class Failure {
    object NetworkTimeout : Failure()
    object ServerError : Failure()
}
