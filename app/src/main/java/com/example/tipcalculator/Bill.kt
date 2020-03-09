package com.example.tipcalculator

class Bill(amount: Double, tip_percent: Int, noPeople: Int) {
    private val amount: Double
    var tipAmount = 0.0
        private set
    var totalAmount = 0.0
        private set
    var amountPerPerson = 0.0
        private set
    private val tip_percent: Int
    private val noPeople: Int
    private fun calculate() {
        tipAmount = tip_percent * amount / 100.0
        totalAmount = tipAmount + amount
        amountPerPerson = totalAmount / noPeople
    }

    inner class ZeroAmountException : Exception()

    init {
        if (amount == 0.0) throw ZeroAmountException()
        this.amount = amount
        this.tip_percent = tip_percent
        this.noPeople = noPeople
        calculate()
    }
}