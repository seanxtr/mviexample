package com.adammcneilly.mviexample.ui.login

import com.adammcneilly.mviexample.ui.redux.Action

/**
 * These are all of the possible actions that can be triggered from the login screen.
 */
sealed class LoginAction : Action {
    data class EmailChanged(val newEmail: String) : LoginAction()
    data class PasswordChanged(val newPassword: String) : LoginAction()
    object SignInButtonClicked : LoginAction()
    object LoginStarted : LoginAction()
    object LoginCompleted : LoginAction()
    data class LoginFailed(val error: Throwable?) : LoginAction()
    data class InvalidEmailSubmitted(val error: String?): LoginAction()

}