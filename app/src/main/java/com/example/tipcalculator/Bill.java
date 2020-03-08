package com.example.tipcalculator;

public class Bill {

    private double amount, tipAmount, totalAmount, amountPerPerson;
    private int tip_percent, noPeople;

    public Bill(double amount, int tip_percent, int noPeople) {
        this.amount = amount;
        this.tip_percent = tip_percent;
        this.noPeople = noPeople;
        calculate();
    }

    private void calculate() {
        tipAmount = tip_percent * amount / 100.0;
        totalAmount = tipAmount + amount;
        amountPerPerson = totalAmount / noPeople;
    }

    public double getTipAmount() {
        return tipAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getAmountPerPerson() {
        return amountPerPerson;
    }
}
