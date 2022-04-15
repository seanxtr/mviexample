package com.adammcneilly.mviexample

import android.util.Log
import com.adammcneilly.mviexample.ui.redux.Action
import com.adammcneilly.mviexample.ui.redux.Middleware
import com.adammcneilly.mviexample.ui.redux.State
import com.adammcneilly.mviexample.ui.redux.Store

/**
 * This [LoggingMiddleware] is responsible for logging every [Action] that is processed to the layout
 * so that we can see the flow of our data for debugging purposes.
 */
class LoggingMiddleware<S:State, A:Action> : Middleware<S, A> {
    override suspend fun process(action: A, currentState: S, store: Store<S, A>) {
        Log.v("LoggingMiddleware", "Processing action: $action, Current state: $currentState")
    }
}