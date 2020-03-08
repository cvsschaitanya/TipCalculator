package com.example.tipcalculator;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class InputPanel {

    MainActivity mainActivity;

    boolean decimal = false;

    TextView billView;

    Button[] numpad;
    Button twoZeroesButton,decimal_pointButton,correctionButton;

    Toast lengthExceededToast;

    InputPanel(MainActivity mainActivity1){
        mainActivity = mainActivity1;
        billView = mainActivity.findViewById(R.id.bill_view);


        numpad = new Button[10];

        numpad[0]=mainActivity.findViewById(R.id.b0);
        numpad[1]=mainActivity.findViewById(R.id.b1);
        numpad[2]=mainActivity.findViewById(R.id.b2);
        numpad[3]=mainActivity.findViewById(R.id.b3);
        numpad[4]=mainActivity.findViewById(R.id.b4);
        numpad[5]=mainActivity.findViewById(R.id.b5);
        numpad[6]=mainActivity.findViewById(R.id.b6);
        numpad[7]=mainActivity.findViewById(R.id.b7);
        numpad[8]=mainActivity.findViewById(R.id.b8);
        numpad[9]=mainActivity.findViewById(R.id.b9);

        twoZeroesButton = mainActivity.findViewById(R.id.b00);
        decimal_pointButton = mainActivity.findViewById(R.id.b_decimal_point);
        correctionButton = mainActivity.findViewById(R.id.buttonclear);

        lengthExceededToast = Toast.makeText(mainActivity,"You have reached the maximum number of digits!",Toast.LENGTH_SHORT);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        for(int i=0;i<=9;++i){
            numpad[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button)view;
                    String digit_text = button.getText().toString().trim();
                    int digit = Integer.parseInt(digit_text);


                    String presentvalue = getPresentValue();
                    if(!decimal&&presentvalue.length()>=6){
                        if(!lengthExceededToast.getView().isShown())
                            lengthExceededToast.show();
                        return;
                    }
                    else if(decimal){
                        int pointindex = presentvalue.indexOf('.');
                        if(presentvalue.length()-pointindex-1>=2){
                            if(!lengthExceededToast.getView().isShown())
                                lengthExceededToast.show();
                            return;
                        }
                    }

                    if(presentvalue.equalsIgnoreCase("0"))
                        presentvalue="";

                    String newValue = presentvalue + digit;
                    postValue(newValue);

                }
            });
        }


        twoZeroesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newValue=null,presentvalue = getPresentValue();

                if(!decimal&&presentvalue.length()>=5){
                    if(!lengthExceededToast.getView().isShown())
                        lengthExceededToast.show();
                    return;
                }
                else if(decimal){
                    int pointindex = presentvalue.indexOf('.');
                    int afterDecimalDigits = presentvalue.length()-pointindex-1;
                    switch(afterDecimalDigits){
                        case 1:newValue=presentvalue+"0";break;
                        case 2:
                            if(!lengthExceededToast.getView().isShown())
                                lengthExceededToast.show();
                            return;
                    }
                }

                if(presentvalue.equalsIgnoreCase("0"))
                    newValue = presentvalue;

                if(newValue==null)
                    newValue = presentvalue + "00";

                postValue(newValue);
            }
        });


        decimal_pointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(decimal)
                    return;

                decimal = true;
                String presentvalue = getPresentValue();
                String newValue = presentvalue + ".";
                postValue(newValue);
            }
        });


        correctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String presentvalue = getPresentValue();


                if(presentvalue.equalsIgnoreCase("0"))
                    return;

                if(presentvalue.isEmpty())
                    return;

                char last = presentvalue.charAt(presentvalue.length()-1);
                if(last=='.')
                    decimal = false;
                String newValue = presentvalue.substring(0,presentvalue.length()-1);
                if(newValue.isEmpty())
                    newValue="0";
                postValue(newValue);
            }
        });
    }


    public String getPresentValue(){
        String str = billView.getText().toString();
        str = str.substring(BillFormatter.CURRENCY_SYMBOL.length());
        return str;
    }

    public void postValue(String newStr){
        newStr = BillFormatter.CURRENCY_SYMBOL + newStr;
        billView.setText(newStr);
    }


    public void clear() {
        decimal = false;
    }
}
