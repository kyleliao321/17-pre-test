package com.a17_pre_test.data.page_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.a17_pre_test.domain.models.GithubUserDomainModel
import com.a17_pre_test.domain.repository.Repository
import com.a17_pre_test.utils.response.Response
import java.net.SocketTimeoutException

class GithubUserPagingSource(
    private val repository: Repository,
    private val matchPattern: String
) : PagingSource<Int, GithubUserDomainModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserDomainModel> {
        val nextPageNumber = params.key ?: 1
        val res = repository.getUserByName(matchPattern, 100, nextPageNumber)

        return if (res is Response.Success) {
            val domainModels = res.data.map { GithubUserDomainModel.from(it) }
            LoadResult.Page(
                data = domainModels,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } else {
            LoadResult.Error(SocketTimeoutException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUserDomainModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}