package com.example.android.architecture.blueprints.movies.login

import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: MoviesRepository) : ViewModel() {

    private val _loginFormReady = MutableLiveData<Boolean>()
    val loginFormReady: LiveData<Boolean> = _loginFormReady

    private val _loginResult = MutableLiveData<Result<Boolean>>()
    val loginResult: LiveData<Result<Boolean>> = _loginResult

    private lateinit var userEmail: String

    fun login() {
        val email = userEmail
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            _loginResult.value = loginRepository.login(email)
        }
    }

    fun loginDataChanged(editable: Editable?) {
        userEmail = editable.toString()
        _loginFormReady.value = isUserEmailValid(userEmail)
    }

    private fun isUserEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }
}
