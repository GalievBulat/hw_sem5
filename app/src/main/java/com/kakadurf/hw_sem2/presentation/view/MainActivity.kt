package com.kakadurf.hw_sem2.presentation.view

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kakadurf.hw_sem2.databinding.MainActivityBinding
import com.kakadurf.hw_sem2.presentation.intent.Action
import com.kakadurf.hw_sem2.presentation.view_model.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    lateinit var binding: MainActivityBinding
    lateinit var operandMap: Map<Int, EditText>
    var lastChange: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val o1 = binding.etOperand1
        val o2 = binding.etOperand2
        val o3 = binding.etOperand3
        operandMap = mapOf(1 to o1, 2 to o2, 3 to o3)

        o1.subscribe()
        o2.subscribe()
        o3.subscribe()
        viewModel.result.observe(this) {
            if (it.isCalculating) {
                binding.progressBar.isVisible = true
            } else {
                binding.progressBar.isVisible = false
                if (it.currentOperandNum != null && it.lastCalculation != null)
                    operandMap[it.currentOperandNum]?.setText(
                        it.lastCalculation.toString(), TextView.BufferType.EDITABLE
                    )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.finalize()
    }

    private fun EditText.subscribe() {
        this.setOnEditorActionListener { textView, i, keyEvent ->

            when (i) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                    val num = operandMap.filterValues { it == this }.keys.first()
                    val other = operandMap.filterValues { it != this }.toList()
                    if ((other[1].second.text.isBlank() || lastChange == other[0].first) && this.text.isNotBlank() && (other[0].second).text.isNotBlank())
                        viewModel.sendAction(
                            Action.TriggerOperation(
                                operands = if (num != 3)
                                    Pair(
                                        (other[0].second).text.toString().toInt(),
                                        this.text.toString().toInt()
                                    )
                                else
                                    Pair(
                                        this.text.toString().toInt(),
                                        (other[0].second).text.toString().toInt()
                                    ),
                                resultingOperandNum = other[1].first
                            )
                        )
                    else if ((other[0].second.text.isBlank() || lastChange == other[1].first) && this.text.isNotBlank() && (other[1].second).text.isNotBlank())
                        viewModel.sendAction(
                            Action.TriggerOperation(
                                operands = if (num != 3)
                                    Pair(
                                        (other[1].second).text.toString().toInt(),
                                        this.text.toString().toInt()
                                    )
                                else
                                    Pair(
                                        this.text.toString().toInt(),
                                        (other[1].second).text.toString().toInt()
                                    ),
                                resultingOperandNum = other[0].first
                            )
                        )
                    lastChange = num
                    true
                }
                else -> false

            }

        }
    }

}