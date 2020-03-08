package com.example.tipcalculator

class BillFormatter(str_amount: String, tipPercent: Int, nPerson: Int) {
    private val bill: Bill
    private fun getAmount(str_amount: String): Double {
        var str_amount = str_amount
        str_amount = str_amount.trim { it <= ' ' }
        return str_amount.toDouble()
    }

    val tipAmount: String
        get() {
            var tip = bill.tipAmount
            tip = roundOffToTwoDigits(tip)
            return getStringFromMonetaryValue(tip)
        }

    val totalAmount: String
        get() {
            var total = bill.totalAmount
            total = roundOffToTwoDigits(total)
            return getStringFromMonetaryValue(total)
        }

    val amountPerPerson: String
        get() {
            var amtPP = bill.amountPerPerson
            amtPP = roundOffToTwoDigits(amtPP)
            return getStringFromMonetaryValue(amtPP)
        }

    companion object {
        const val CURRENCY_SYMBOL = "$"
        @JvmStatic
        fun getPersonString(nP: Int): String {
            return "People:$nP"
        }

        @JvmStatic
        fun getTipString(tip: Int): String {
            return "Tip $tip%"
        }

        fun getStringFromMonetaryValue(amount: Double): String {
            var amountStr = amount.toString()
            amountStr = CURRENCY_SYMBOL + amountStr
            return amountStr
        }

        fun roundOffToTwoDigits(d: Double): Double {
            var d = d
            d *= 100.0
            d = Math.round(d).toDouble()
            d = d / 100
            return d
        }
    }

    init {
        val amount = getAmount(str_amount)
        bill = Bill(amount, tipPercent, nPerson)
    }
}