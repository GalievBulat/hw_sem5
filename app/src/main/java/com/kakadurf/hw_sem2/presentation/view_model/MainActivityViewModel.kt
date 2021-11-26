package com.kakadurf.hw_sem2.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.reduxStore
import com.kakadurf.hw_sem2.data.CalculationProcessor
import com.kakadurf.hw_sem2.presentation.intent.Action
import com.kakadurf.hw_sem2.presentation.intent.Reducer
import com.kakadurf.hw_sem2.presentation.intent.SideEffect
import com.kakadurf.hw_sem2.presentation.intent.State
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainActivityViewModel : ViewModel() {
    private val _result: MutableLiveData<State> = MutableLiveData()
    val result: LiveData<State> = _result

    private val actions: Subject<Action> = PublishSubject.create()
    private val sideEffects: List<SideEffect<State, Action>> =
        listOf(SideEffect(CalculationProcessor()))
    private var calc: Disposable? = null

    init {
        calc = actions
            .reduxStore(
                State(null, null, null, false, null), sideEffects, Reducer()
            )
            .subscribe {
                _result.postValue(it)
            }
    }

    fun finalize() {
        calc?.dispose()
    }

    fun sendAction(action: Action) {
        actions.onNext(action)
    }

}