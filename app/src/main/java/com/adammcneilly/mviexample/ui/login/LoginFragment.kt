package com.adammcneilly.mviexample.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.adammcneilly.mviexample.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // whenever the view is resumed, subscribe to our viewmodel's ViewState state flow
        lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect { it ->
                processViewState(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Whenever a relevant UI action occurs like a text change or a button click,
     * proxy that to the view model handle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailInput.doOnTextChanged { text, _, _, _ ->
            viewModel.emailChanged(text.toString())
        }

        binding.passwordInput.doOnTextChanged { text, _, _, _ ->
            viewModel.passwordChanged(text.toString())
        }

        binding.signInButton.setOnClickListener {
            viewModel.signInButtonClicked()
        }
    }

    fun processViewState(viewState: LoginViewState) {
        binding.progressBar.visibility = if (viewState.showProgressBar) View.VISIBLE else View.GONE

        with(binding.emailInputLayout) {
            isErrorEnabled = viewState.emailError != null
            error = viewState.emailError
        }

        binding.passwordInputLayout.error = viewState.passwordError
    }
}