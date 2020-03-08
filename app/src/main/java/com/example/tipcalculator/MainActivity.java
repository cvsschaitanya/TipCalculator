package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    InputPanel inputPanel;

    TextView tv_NoPersons, tv_amount_person, tv_total_amount, tv_tip_amount, tv_tip_percent;
    TextView bill_view;
    Button addPerson_button, removePerson_button, calc_button, reset_button;
    SeekBar sb_Tip,sb_persons;

    Toast maxToast,minToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_tip_percent = findViewById(R.id.tvTipPercentage);
        tv_tip_amount = findViewById(R.id.tv_tip_amount);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_amount_person = findViewById(R.id.tvAmountPerson);
        tv_NoPersons = findViewById(R.id.NoPersons);

        bill_view = findViewById(R.id.bill_view);

        addPerson_button = findViewById(R.id.addperson);
        removePerson_button = findViewById(R.id.removeperson);

        calc_button = findViewById(R.id.btnCalculate);
        reset_button = findViewById(R.id.btnReset);

        inputPanel = new InputPanel(this);

        sb_Tip = findViewById(R.id.sb_Tip);
        sb_Tip.setMax(25);
        sb_Tip.setProgress(15);

        sb_Tip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String out = "Tip " + i + "%";
                tv_tip_percent.setText(out);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_persons = findViewById(R.id.persons_bar);
        sb_persons.setMax(100);
        sb_persons.setProgress(1);

        sb_persons.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<1)
                    seekBar.setProgress(1);
                else
                    setPersons(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxToast = Toast.makeText(MainActivity.this,"Maximum 100 persons limit.",Toast.LENGTH_SHORT);
        minToast = Toast.makeText(MainActivity.this,"Minimum 1 person required.",Toast.LENGTH_SHORT);

        addPerson_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persons = getPersons();

                if(persons<100) {
                    persons++;
                    sb_persons.setProgress(persons);
                }
                else{
                    if(!maxToast.getView().isShown())
                        maxToast.show();
                }

                setPersons(persons);
            }
        });

        removePerson_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persons = getPersons();

                if(persons>1){
                    persons--;
                    sb_persons.setProgress(persons);
                }
                else{
                    if(!minToast.getView().isShown())
                        minToast.show();
                }

                setPersons(persons);
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPanel.clear();

                bill_view.setText(BillFormatter.CURRENCY_SYMBOL +"0");
                tv_tip_amount.setText(BillFormatter.CURRENCY_SYMBOL +"0.0");
                tv_amount_person.setText(BillFormatter.CURRENCY_SYMBOL +"0.0");
                tv_total_amount.setText(BillFormatter.CURRENCY_SYMBOL +"0.0");
                sb_Tip.setProgress(15);
                sb_persons.setProgress(1);

                tv_tip_percent.setText("Tip 15%");

                tv_NoPersons.setText("1 Person");
            }
        });

        calc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResult();
            }
        });

    }


    private void showResult() {



        String strAmount = inputPanel.getPresentValue();
        int tipPercent = sb_Tip.getProgress();
        String NPerson = tv_NoPersons.getText().toString();

        BillFormatter billFormatter = new BillFormatter(strAmount, tipPercent, NPerson);


        tv_tip_amount.setText(String.valueOf(billFormatter.getTipAmount()));
        tv_total_amount.setText(String.valueOf(billFormatter.getTotalAmount()));
        tv_amount_person.setText(String.valueOf(billFormatter.getAmountPerPerson()));
    }

    private int getPersons(){
        return BillFormatter.getPerson(tv_NoPersons.getText().toString());
    }

    private void setPersons(int persons){
        String out = BillFormatter.getPersonString(persons);
        tv_NoPersons.setText(out);
    }
}

