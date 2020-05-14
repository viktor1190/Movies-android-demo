package com.example.android.architecture.blueprints.movies.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.architecture.blueprints.movies.R
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.isLoading
import com.example.android.architecture.blueprints.movies.data.succeeded
import com.example.android.architecture.blueprints.movies.databinding.FragmentLoginBinding
import com.example.android.architecture.blueprints.movies.login.ui.login.LoginViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentLoginBinding

            override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            setViewInLoadingState(loginResult.isLoading)
            if (loginResult.succeeded) {
                navigateToUserHome()
            } else if (loginResult is Result.Error){
                showLoginFailed(loginResult.message ?: loginResult.exception.message)
            }
        })
    }

    private fun navigateToUserHome() {
        Toast.makeText(
                requireContext(),
                R.string.welcome,
                Toast.LENGTH_LONG
        ).show()
        findNavController().navigate(R.id.moviesListFragment)
    }

    private fun showLoginFailed(errorString: String?) {
        Toast.makeText(requireContext(), errorString ?: getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
    }

    private fun setViewInLoadingState(enable: Boolean) {
        viewDataBinding.loading.visibility = if (enable) View.VISIBLE else View.GONE
        viewDataBinding.buttonLogin.isEnabled = !enable
        viewDataBinding.inputEmail.isEnabled = !enable

    }
}
