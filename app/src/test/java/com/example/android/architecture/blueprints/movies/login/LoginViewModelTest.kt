package com.example.android.architecture.blueprints.movies.login

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.movies.LiveDataTestUtil
import com.example.android.architecture.blueprints.movies.MainCoroutineRule
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.isLoading
import com.example.android.architecture.blueprints.movies.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

private const val EMAIL = "test@testing.com"

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    // SUT
    private lateinit var loginViewModel: LoginViewModel

    // fake repository
    private lateinit var moviesRepository: FakeRepository

    @Mock
    private lateinit var editableInputEmail: Editable

    // set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        moviesRepository = FakeRepository()

        loginViewModel = LoginViewModel(moviesRepository)
        Mockito.`when`(editableInputEmail.toString()).thenReturn(EMAIL)
    }

    @Test
    fun `When user submit the email, Should emmit loading state and Then login the user successfully`() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()
        loginViewModel.loginDataChanged(editableInputEmail)
        // checking current state during submit
        assertThat(LiveDataTestUtil.getValue(loginViewModel.loginFormReady)).isTrue()

        // Submit the login and checks loading state arose
        loginViewModel.login()
        mainCoroutineRule.advanceUntilIdle()
        val loadingState = LiveDataTestUtil.getValue(loginViewModel.loginResult) as Result.Success
        assertThat(loadingState.data).isTrue()

        // Resumes the coroutines as normally and checks the submit result
        mainCoroutineRule.resumeDispatcher()
        assertThat(loginViewModel.loginResult.value?.isLoading).isFalse()
        val loginResult = LiveDataTestUtil.getValue(loginViewModel.loginResult) as Result.Success
        assertThat(loginResult.data).isTrue()
    }
}