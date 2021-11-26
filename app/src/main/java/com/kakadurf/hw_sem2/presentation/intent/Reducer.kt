package com.kakadurf.hw_sem2.presentation.intent

import com.freeletics.rxredux.Reducer

class Reducer : Reducer<State, Action> {
    override fun invoke(state: State, action: Action): State {
        println("ChangeState")
        return when (action) {
            is Action.OperationCalculatingAction -> state.copy(
                isCalculating = true,
                currentOperandNum = null
            )
            is Action.ErrorCalculatingAction -> state.copy(
                isCalculating = false,
                error = action.error
            )
            is Action.OperationDoneAction -> state.copy(
                isCalculating = false,
                error = null,
                currentOperandNum = action.position,
                lastCalculation = action.result
            )
            else -> state
        }
    }
}