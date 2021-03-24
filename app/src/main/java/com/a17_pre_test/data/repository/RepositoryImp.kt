package com.a17_pre_test.data.repository

import com.a17_pre_test.data.models.GithubUserDataModel
import com.a17_pre_test.domain.data_source.RemoteDataSource
import com.a17_pre_test.domain.repository.Repository
import com.a17_pre_test.utils.response.Response

class RepositoryImp(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getUserByName(
        matchPattern: String,
        pageSize: Int,
        page: Int
    ): Response<List<GithubUserDataModel>> =
        remoteDataSource.getUserNameByName(matchPattern, pageSize, page)
}