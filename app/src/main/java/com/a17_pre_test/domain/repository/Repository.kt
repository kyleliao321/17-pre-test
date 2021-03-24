package com.a17_pre_test.domain.repository

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.utils.response.Response

interface Repository {
    suspend fun getUserByName(
        matchPattern: String,
        pageSize: Int,
        page: Int
    ): Response<List<GithubUserDataModel>>
}