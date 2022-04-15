package com.adammcneilly.mviexample.ui.redux

import com.adammcneilly.mviexample.LoggingMiddleware
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A [Store] is a our state container for a given screen
 *
 * @param[initialState] this is the initial state of the screen when it is first created
 * @param[reducer] A system for taking in the current state, and a new action, and outputting the
 * updated state.
 * @param[middlewares] This is a list of [Middleware] entities for handling any side effects for
 * actions dispatched to this store.
 */
class Store<S : State, A : Action>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<S, A>> = listOf()
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    suspend fun dispatch(action: A) {

        middlewares.forEach { it.process(action, state.value, this) }

        val newState = reducer.reduce(state.value, action)
        _state.value = newState
    }
}