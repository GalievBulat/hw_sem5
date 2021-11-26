package com.kakadurf.hw_sem2.presentation.intent

import com.freeletics.rxredux.SideEffect
import com.kakadurf.hw_sem2.data.CalculationProcessor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SideEffect<T, U>(private val calculationProcessor: CalculationProcessor) :
    SideEffect<State, Action> {

    override fun invoke(actions: Observable<Action>, state: () -> State): Observable<out Action> =
        actions
            .ofType(Action.TriggerOperation::class.java)
            .switchMap {
                calculationProcessor.performCalculation(
                    if (it.resultingOperandNum != 3)
                        CalculationProcessor.CalculationType.DIFF
                    else
                        CalculationProcessor.CalculationType.SUM,
                    it.operands.first,
                    it.operands.second
                )
                    .map<Action> { result: Int ->
                        Action.OperationDoneAction(
                            it.resultingOperandNum, result
                        )
                    }
                    .onErrorReturn { error: Throwable -> Action.ErrorCalculatingAction(error) }
                    .delay(3000, TimeUnit.MILLISECONDS)
                    .startWith(Action.OperationCalculatingAction)
            }


}