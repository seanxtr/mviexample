package com.adammcneilly.mviexample

import androidx.lifecycle.LifecycleCoroutineScope
import com.adammcneilly.mviexample.ui.login.LoginAction
import com.adammcneilly.mviexample.ui.login.LoginViewState
import com.adammcneilly.mviexample.ui.redux.Middleware
import com.adammcneilly.mviexample.ui.redux.Store
import kotlinx.coroutines.*
import java.lang.Exception

class LoginNetworkMiddleware(private val loginRepository: LoginRepository) :
    Middleware<LoginViewState, LoginAction> {

    override suspend fun process(
        action: LoginAction,
        currentState: LoginViewState,
        store: Store<LoginViewState, LoginAction>
    ) {
        when (action) {
            is LoginAction.SignInButtonClicked -> {
                if (currentState.email.isEmpty()) {
                    store.dispatch(LoginAction.InvalidEmailSubmitted("Please enter an email address"))
                    return
                } else {
                    store.dispatch(LoginAction.InvalidEmailSubmitted(null))
                }

                loginUser(store, currentState)
            }
            else -> {}
        }
    }

    private suspend fun loginUser(
        store: Store<LoginViewState, LoginAction>,
        currentState: LoginViewState
    ) {
        store.dispatch(LoginAction.LoginStarted)

        delay(2000L)
        val isSuccessful = loginRepository.login(
            currentState.email, currentState.password
        )

        if (isSuccessful) {
            store.dispatch(LoginAction.LoginCompleted)
        } else {
            store.dispatch(LoginAction.LoginFailed(null))
        }
    }
}