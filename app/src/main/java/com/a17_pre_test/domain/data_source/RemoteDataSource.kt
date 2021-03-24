package com.a17_pre_test.domain.data_source

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.utils.response.Response

interface RemoteDataSource {
    suspend fun getUserNameByName(
        matchPattern: String,
        pageSize: Int,
        page: Int
    ): Response<List<GithubUserDataModel>>
}