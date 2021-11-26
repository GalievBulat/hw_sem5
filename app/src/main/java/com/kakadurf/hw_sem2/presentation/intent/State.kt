package com.kakadurf.hw_sem2.presentation.intent

data class State(
    val prevOperands: Pair<Int, Int>?,
    val currentOperandNum: Int?,
    val lastCalculation: Int?,
    val isCalculating: Boolean,
    val error: Throwable?
)