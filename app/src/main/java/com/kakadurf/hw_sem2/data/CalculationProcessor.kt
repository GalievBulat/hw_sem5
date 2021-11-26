package com.kakadurf.hw_sem2.data

import io.reactivex.Observable


class CalculationProcessor {
    fun performCalculation(type: CalculationType, int1: Int, int2: Int) =
        when (type) {
            CalculationType.SUM -> Observable.just(int1 + int2)
            CalculationType.DIFF -> Observable.just(int1 - int2)
        }

    enum class CalculationType {
        SUM, DIFF
    }
}