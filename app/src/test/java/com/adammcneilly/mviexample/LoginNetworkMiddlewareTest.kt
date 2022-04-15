package com.adammcneilly.mviexample

import com.adammcneilly.mviexample.ui.login.LoginAction
import com.adammcneilly.mviexample.ui.login.LoginReducer
import com.adammcneilly.mviexample.ui.login.LoginViewState
import com.adammcneilly.mviexample.ui.redux.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoginNetworkMiddlewareTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    init {
        MockKAnnotations.init(this)
    }

    /**
     * When running this test, we want to ensure that after having [LoginNetworkingMiddleware] process
     * the [LoginAction.SignInButtonClicked] action when the email is invalid, it should then
     * have the store process the [LoginAction.InvalidEmailSubmitted] action.
     */
    @Test
    fun submitInvalidEmail() {
        // Givens
        val fakeLoginRepository = FakeLoginRepository()
        fakeLoginRepository.shouldMockSuccess = true

        val inputState = LoginViewState(email = "")
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkMiddleware(fakeLoginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginViewState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.InvalidEmailSubmitted(""))
    }

    /**
     * When the [LoginNetworkingMiddleware] processes the [LoginAction.SignInButtonClicked] action
     * and there are valid inputs, we want to ensure that the corresponding actions are sent back
     * into the store.
     */
    @Test
    fun validLogin() {
        // Givens
        coEvery { loginRepository.login("seanxtr@gmail.com","123") }.returns(true)

        val inputState = LoginViewState(
            email = "seanxtr@gmail.com",
            password = "123",
        )
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkMiddleware(loginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginViewState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginStarted)
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginCompleted)
    }

    @Test
    fun loginFailure() {
        // Givens
        val fakeLoginRepository = FakeLoginRepository()
        fakeLoginRepository.shouldMockSuccess = false

        val inputState = LoginViewState(
            email = "testy@mctestface.com",
            password = "hunter2",
        )
        val inputAction = LoginAction.SignInButtonClicked

        val middlewareUnderTest = LoginNetworkMiddleware(fakeLoginRepository)
        val actionCaptureMiddleware = ActionCaptureMiddleware<LoginViewState, LoginAction>()

        val loginStore = Store(
            inputState,
            LoginReducer(),
            listOf(actionCaptureMiddleware),
        )

        // When
        runBlocking {
            middlewareUnderTest.process(inputAction, inputState, loginStore)
        }

        // Then
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginStarted)
        actionCaptureMiddleware.assertActionProcessed(LoginAction.LoginFailed(null))
    }
}
