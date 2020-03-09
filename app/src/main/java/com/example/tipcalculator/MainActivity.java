package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    InputPanel inputPanel;

    TextView tv_NoPersons, tv_amount_person, tv_total_amount, tv_tip_amount, tv_tip_percent;
    TextView bill_view;
    Button addPerson_button, removePerson_button, increaseTip_button, reduceTip_button, calc_button, reset_button;
    SeekBar sb_Tip, sb_persons;

    Toast personMaxToast, personMinToast, tipMaxToast, tipMinToast;

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
        increaseTip_button = findViewById(R.id.increaseTip);
        reduceTip_button = findViewById(R.id.reduceTip);

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

        tipMaxToast = Toast.makeText(MainActivity.this, "Maximum tip is 25%.", Toast.LENGTH_SHORT);
        tipMinToast = Toast.makeText(MainActivity.this, "Minimum tip is 0%.", Toast.LENGTH_SHORT);

        increaseTip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tip = getTip();

                if (tip < 25) {
                    tip++;
                    sb_Tip.setProgress(tip);
                    setTip(tip);
                } else {
                    if (!tipMaxToast.getView().isShown())
                        tipMaxToast.show();
                }
            }
        });

        reduceTip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tip = getTip();

                if (tip > 0) {
                    tip--;
                    sb_Tip.setProgress(tip);
                    setTip(tip);
                } else {
                    if (!tipMinToast.getView().isShown())
                        tipMinToast.show();
                }
            }
        });


        sb_persons = findViewById(R.id.persons_bar);
        sb_persons.setMax(100);
        sb_persons.setProgress(1);

        sb_persons.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1)
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

        personMaxToast = Toast.makeText(MainActivity.this, "Maximum 100 persons limit.", Toast.LENGTH_SHORT);
        personMinToast = Toast.makeText(MainActivity.this, "Minimum 1 person required.", Toast.LENGTH_SHORT);

        addPerson_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persons = getPersons();

                if (persons < 100) {
                    persons++;
                    sb_persons.setProgress(persons);
                } else {
                    if (!personMaxToast.getView().isShown())
                        personMaxToast.show();
                }

                setPersons(persons);
            }
        });

        removePerson_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int persons = getPersons();

                if (persons > 1) {
                    persons--;
                    sb_persons.setProgress(persons);
                } else {
                    if (!personMinToast.getView().isShown())
                        personMinToast.show();
                }

                setPersons(persons);
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPanel.clear();

                if(bill_view.getError()!=null) {
                    bill_view.setError(null);
                    bill_view.clearFocus();
                }

                bill_view.setText(BillFormatter.CURRENCY_SYMBOL + "0");
                tv_tip_amount.setText(BillFormatter.CURRENCY_SYMBOL + "0");
                tv_amount_person.setText(BillFormatter.CURRENCY_SYMBOL + "0");
                tv_total_amount.setText(BillFormatter.CURRENCY_SYMBOL + "0");
                sb_Tip.setProgress(15);
                sb_persons.setProgress(1);

                tv_tip_percent.setText("Tip 15%");

                tv_NoPersons.setText("People:1");
            }
        });

        calc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bill_view.getError()!=null) {
                    bill_view.setError(null);
                    bill_view.clearFocus();
                }

                showResult();
            }
        });

    }


    private void showResult() {


        String strAmount = inputPanel.getPresentValue();
        int tipPercent = sb_Tip.getProgress();
        int NPerson = sb_persons.getProgress();

        BillFormatter billFormatter = null;
        try {
            billFormatter = new BillFormatter(strAmount, tipPercent, NPerson);

            tv_tip_amount.setText(String.valueOf(billFormatter.getTipAmount()));
            tv_total_amount.setText(String.valueOf(billFormatter.getTotalAmount()));
            tv_amount_person.setText(String.valueOf(billFormatter.getAmountPerPerson()));

        } catch (Bill.ZeroAmountException e) {
            inputPanel.zeroInput();
        }
    }

    private int getPersons() {
        return sb_persons.getProgress();
    }

    private void setPersons(int persons) {
        String out = BillFormatter.getPersonString(persons);
        tv_NoPersons.setText(out);
    }

    private int getTip() {
        return sb_Tip.getProgress();
    }

    private void setTip(int tip) {
        String out = BillFormatter.getTipString(tip);
        tv_tip_percent.setText(out);
    }
}
