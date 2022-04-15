package com.adammcneilly.mviexample.ui.login

import android.util.Log
import com.adammcneilly.mviexample.ui.redux.Reducer

/**
 * This reducer is responsible for handling any [LoginAction], and using that to create
 * a new [LoginViewState].
 */
class LoginReducer : Reducer<LoginViewState, LoginAction> {

    /**
     * Side note: Notice that all of the functions are named in a way that they signify they're
     * returning a new state, and not just processing information. This helps keep your new statements
     * clear that they are returning stuff, so that context isn't lost.
     */
    override fun reduce(currentState: LoginViewState, action: LoginAction): LoginViewState {
        return when (action) {
            is LoginAction.EmailChanged -> {
                stateWithEmailChanged(currentState, action)
            }
            is LoginAction.PasswordChanged -> {
                stateWithNewPassword(currentState, action)
            }
            is LoginAction.LoginFailed -> {
                stateAfterLoginFailed(currentState)
            }
            LoginAction.SignInButtonClicked -> {
                currentState
            }
            LoginAction.LoginStarted -> {
                stateAfterLoginStarted(currentState)
            }
            LoginAction.LoginCompleted -> {
                stateAfterLoginCompleted(currentState)
            }
            is LoginAction.InvalidEmailSubmitted -> {
                stateWithInvalidEmail(currentState, action)
            }
        }
    }

    private fun stateAfterLoginStarted(currentState: LoginViewState) =
        currentState.copy(
            showProgressBar = true
        )

    private fun stateAfterLoginFailed(currentState: LoginViewState) =
        currentState.copy(
            showProgressBar = false
        )

    private fun stateAfterLoginCompleted(currentState: LoginViewState) =
        currentState.copy(
            showProgressBar = false
        )

    private fun stateWithNewPassword(
        currentState: LoginViewState,
        action: LoginAction.PasswordChanged
    ) = currentState.copy(
        password = action.newPassword
    )

    private fun stateWithEmailChanged(
        currentState: LoginViewState, action: LoginAction.EmailChanged
    ) = currentState.copy(
        email = action.newEmail
    )

    private fun stateWithInvalidEmail(
        currentState: LoginViewState,
        action: LoginAction.InvalidEmailSubmitted
    ) = currentState.copy(
        emailError = action.error
    )


}