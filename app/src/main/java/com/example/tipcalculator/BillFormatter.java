package com.example.tipcalculator;


import java.util.StringTokenizer;

public class BillFormatter {

    public final static String CURRENCY_SYMBOL = "$";

    private Bill bill;

    public BillFormatter(String str_amount, int tipPercent, String str_nPerson) {
        Double amount = getAmount(str_amount);
        int nP = getPerson(str_nPerson);

        bill = new Bill(amount, tipPercent, nP);
    }

    public static int getPerson(String str_nPerson) {
        int spaceindex = str_nPerson.indexOf(' ');
        str_nPerson = str_nPerson.substring(0,spaceindex).trim();
        int n = Integer.parseInt(str_nPerson);
        return n;
    }

    public static String getPersonString(int nP) {
        String out = nP+" Person";
        if(nP>1)
            out = out + "s";
        return out;
    }

    private Double getAmount(String str_amount) {
        str_amount = str_amount.trim();
        double amount = Double.parseDouble(str_amount);
        return amount;
    }

    public String getTipAmount() {
        double tip = bill.getTipAmount();
        tip = BillFormatter.roundOffToTwoDigits(tip);

        String tipStr =  getStringFromMonetaryValue(tip);

        return tipStr;
    }

    public String getTotalAmount() {
        double total = bill.getTotalAmount();
        total = roundOffToTwoDigits(total);

        String totalStr =  getStringFromMonetaryValue(total);

        return totalStr;
    }

    public String getAmountPerPerson() {
        double amtPP = bill.getAmountPerPerson();
        amtPP = roundOffToTwoDigits(amtPP);

        String amtPP_Str =  getStringFromMonetaryValue(amtPP);

        return amtPP_Str ;
    }

    public static String getStringFromMonetaryValue(double amount){
        String amountStr = String.valueOf(amount);

        amountStr = CURRENCY_SYMBOL + amountStr;
        return amountStr;
    }


    public static double roundOffToTwoDigits(double d) {
        d *= 100;
        d = Math.round(d);
        d = d / 100;
        return d;
    }
}
