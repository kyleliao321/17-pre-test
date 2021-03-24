package com.a17_pre_test.presentation.views.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.a17_pre_test.domain.models.GithubUserDomainModel
import com.a17_pre_test.domain.usecases.GetUserByNameFlowUseCase
import com.a17_pre_test.test_utils.CoroutineTestRule
import com.a17_pre_test.utils.failure.Failure
import com.a17_pre_test.utils.response.Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = CoroutineTestRule()

    @MockK(relaxed = true)
    private lateinit var observer: Observer<SearchViewModel.State>

    @MockK
    private lateinit var getUserByNameFlowUseCase: GetUserByNameFlowUseCase

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel =
            SearchViewModel(getUserByNameFlowUseCase, mainCoroutineRule.testCoroutineDispatcher)

        viewModel.stateLiveData.observeForever(observer)
    }

    @After
    fun teardown() {
        viewModel.stateLiveData.removeObserver(observer)
    }

    @Test
    fun `verify state after LoadUserFlow and it fail`() {
        // given
        val name = "q"

        coEvery { getUserByNameFlowUseCase(any()) } returns Response.Failure(Failure.ServerError)

        // when
        viewModel.onIntent(SearchViewModel.Intent.LoadUserFlow(name))

        // then
        verify(exactly = 2) { observer.onChanged(any()) } // loading, loaded
        viewModel.stateLiveData.value shouldBeEqualTo SearchViewModel.State(
            isUserFlowLoading = false,
            userFlow = null
        )
    }

}