package com.example.tipcalculator

import android.view.View
import android.widget.Button
import android.widget.TextView

class InputPanel internal constructor(var mainActivity: MainActivity) {
    val ZERO_INPUT_MESSAGE = "Zero Input not allowed!"
    val MAXIMUM_INPUT_MESSAGE = "You have reached the maximum number of digits!"
    val TWO_DECIMAL_CROSS = "Only 2 digits allowed after decimal!"
    var decimal = false
    var billView: TextView
    var numpad: Array<Button?>
    var twoZeroesButton: Button
    var decimal_pointButton: Button
    var correctionButton: Button
    private fun setOnClickListeners() {
        for (i in 0..9) {
            numpad[i]!!.setOnClickListener(View.OnClickListener { view ->
                val button = view as Button
                val digit_text = button.text.toString().trim { it <= ' ' }
                val digit = digit_text.toInt()

                var presentvalue = presentValue
                if (!decimal && presentvalue.length >= 6) {
                    lengthExceeded()
                    return@OnClickListener
                } else if (decimal) {
                    val pointindex = presentvalue.indexOf('.')
                    if (presentvalue.length - pointindex - 1 >= 2) {
                        decimalLengthExceeded()
                        return@OnClickListener
                    }
                } else if (billView.error != null && billView.error.toString() == MAXIMUM_INPUT_MESSAGE ) {
                    billView.error = null
                    billView.clearFocus()
                }


                if (billView.error != null && billView.error.toString() == ZERO_INPUT_MESSAGE && digit != 0) {
                    billView.error = null
                    billView.clearFocus()
                }

                if (presentvalue.equals("0", ignoreCase = true)) presentvalue = ""
                val newValue = presentvalue + digit
                postValue(newValue)
            })
        }
        twoZeroesButton.setOnClickListener(View.OnClickListener {
            var newValue: String? = null
            val presentvalue = presentValue
            if (!decimal && presentvalue.length >= 5) {
                lengthExceeded()
                return@OnClickListener
            } else if (decimal) {
                val pointindex = presentvalue.indexOf('.')
                val afterDecimalDigits = presentvalue.length - pointindex - 1
                when (afterDecimalDigits) {
                    1 -> newValue = presentvalue + "0"
                    2 -> {
                        decimalLengthExceeded()
                        return@OnClickListener
                    }
                }
            }
            if (presentvalue.equals("0", ignoreCase = true)) newValue = presentvalue
            if (newValue == null) newValue = presentvalue + "00"
            postValue(newValue)
        })
        decimal_pointButton.setOnClickListener(View.OnClickListener {
            if (decimal) return@OnClickListener
            if (billView.error != null && billView.error.toString() == MAXIMUM_INPUT_MESSAGE) {
                billView.error = null
                billView.clearFocus()
            }
            decimal = true
            val presentvalue = presentValue
            val newValue = "$presentvalue."
            postValue(newValue)
        })
        correctionButton.setOnClickListener(View.OnClickListener {
            val presentvalue = presentValue
            if (presentvalue.equals("0", ignoreCase = true)) return@OnClickListener
            if (billView.error != null && billView.error.toString() == MAXIMUM_INPUT_MESSAGE) {
                billView.error = null
                billView.clearFocus()
            }
            if (billView.error != null && billView.error.toString() == TWO_DECIMAL_CROSS) {
                billView.error = null
                billView.clearFocus()
            }
            val last = presentvalue[presentvalue.length - 1]
            if (last == '.') decimal = false
            var newValue = presentvalue.substring(0, presentvalue.length - 1)
            if (newValue.isEmpty()) newValue = "0"
            postValue(newValue)
        })
    }

    val presentValue: String
        get() {
            var str = billView.text.toString()
            str = str.substring(BillFormatter.CURRENCY_SYMBOL.length)
            return str
        }

    fun postValue(newStr: String) {
        var newStr = newStr
        newStr = BillFormatter.CURRENCY_SYMBOL + newStr
        billView.text = newStr
    }

    fun clear() {
        decimal = false
    }

    fun zeroInput() {
        billView.error = ZERO_INPUT_MESSAGE
        billView.requestFocus()
    }

    fun lengthExceeded() {
        billView.error = MAXIMUM_INPUT_MESSAGE
        billView.requestFocus()
    }

    fun decimalLengthExceeded() {
        billView.error = TWO_DECIMAL_CROSS
        billView.requestFocus()
    }

    init {
        billView = mainActivity.findViewById(R.id.bill_view)
        numpad = arrayOfNulls(10)
        numpad[0] = mainActivity.findViewById(R.id.b0)
        numpad[1] = mainActivity.findViewById(R.id.b1)
        numpad[2] = mainActivity.findViewById(R.id.b2)
        numpad[3] = mainActivity.findViewById(R.id.b3)
        numpad[4] = mainActivity.findViewById(R.id.b4)
        numpad[5] = mainActivity.findViewById(R.id.b5)
        numpad[6] = mainActivity.findViewById(R.id.b6)
        numpad[7] = mainActivity.findViewById(R.id.b7)
        numpad[8] = mainActivity.findViewById(R.id.b8)
        numpad[9] = mainActivity.findViewById(R.id.b9)
        twoZeroesButton = mainActivity.findViewById(R.id.b00)
        decimal_pointButton = mainActivity.findViewById(R.id.b_decimal_point)
        correctionButton = mainActivity.findViewById(R.id.buttonclear)
        setOnClickListeners()
    }
}