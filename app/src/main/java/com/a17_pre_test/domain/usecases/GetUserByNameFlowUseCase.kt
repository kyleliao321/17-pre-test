package com.a17_pre_test.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.a17_pre_test.data.page_source.GithubUserPagingSource
import com.a17_pre_test.domain.models.GithubUserDomainModel
import com.a17_pre_test.domain.repository.Repository
import com.a17_pre_test.utils.response.Response
import com.a17_pre_test.utils.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class GetUserByNameFlowUseCase(
    private val repository: Repository,
    defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Flow<PagingData<GithubUserDomainModel>>, GetUserByNameFlowUseCase.Params>(
    defaultDispatcher
) {

    override suspend fun run(params: Params): Response<Flow<PagingData<GithubUserDomainModel>>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 100
            ),
            pagingSourceFactory = { GithubUserPagingSource(repository, params.name) }
        ).flow

        return Response.Success(pager)
    }

    data class Params(val name: String)
}