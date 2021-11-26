package com.kakadurf.hw_sem2.presentation.intent

sealed class Action {
    data class TriggerOperation(
        val operands: Pair<Int, Int>,
        val resultingOperandNum: Int
    ) : Action()

    data class OperationDoneAction(val position: Int, val result: Int) : Action()
    object OperationCalculatingAction : Action()
    data class ErrorCalculatingAction(val error: Throwable) : Action()
}