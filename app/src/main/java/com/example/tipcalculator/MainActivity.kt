package com.example.tipcalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculator.Bill.ZeroAmountException
import com.example.tipcalculator.BillFormatter.Companion.getPersonString
import com.example.tipcalculator.BillFormatter.Companion.getTipString

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_tip_percent = findViewById<TextView>(R.id.tvTipPercentage)
        val tv_tip_amount = findViewById<TextView>(R.id.tv_tip_amount)
        val tv_total_amount = findViewById<TextView>(R.id.tv_total_amount)
        val tv_amount_person = findViewById<TextView>(R.id.tvAmountPerson)
        val tv_NoPersons = findViewById<TextView>(R.id.NoPersons)
        val bill_view = findViewById<TextView>(R.id.bill_view)
        val addPerson_button = findViewById<Button>(R.id.addperson)
        val removePerson_button = findViewById<Button>(R.id.removeperson)
        val increaseTip_button = findViewById<Button>(R.id.increaseTip)
        val reduceTip_button = findViewById<Button>(R.id.reduceTip)
        val calc_button = findViewById<Button>(R.id.btnCalculate)
        val reset_button = findViewById<Button>(R.id.btnReset)
        val inputPanel = InputPanel(this)
        val sb_Tip = findViewById<SeekBar>(R.id.sb_Tip)
        val sb_persons = findViewById<SeekBar>(R.id.persons_bar)

        val tipMaxToast = Toast.makeText(this@MainActivity, "Maximum tip is 25%.", Toast.LENGTH_SHORT)
        val tipMinToast = Toast.makeText(this@MainActivity, "Minimum tip is 0%.", Toast.LENGTH_SHORT)
        val personMaxToast = Toast.makeText(this@MainActivity, "Maximum 100 persons limit.", Toast.LENGTH_SHORT)
        val personMinToast = Toast.makeText(this@MainActivity, "Minimum 1 person required.", Toast.LENGTH_SHORT)


        sb_Tip.setMax(25)
        sb_Tip.setProgress(15)
        sb_Tip.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val out = "Tip $i%"
                tv_tip_percent.setText(out)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        increaseTip_button.setOnClickListener(View.OnClickListener {
            var tip = tip
            if (tip < 25) {
                tip++
                sb_Tip.setProgress(tip)
                tip = tip
            } else {
                if (!tipMaxToast.getView().isShown) tipMaxToast.show()
            }
        })
        reduceTip_button.setOnClickListener(View.OnClickListener {
            var tip = tip
            if (tip > 0) {
                tip--
                sb_Tip.setProgress(tip)
                tip = tip
            } else {
                if (!tipMinToast.getView().isShown) tipMinToast.show()
            }
        })


        sb_persons.setMax(100)
        sb_persons.setProgress(1)
        sb_persons.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i < 1) seekBar.progress = 1 else persons = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        addPerson_button.setOnClickListener(View.OnClickListener {
            var persons = persons
            if (persons < 100) {
                persons++
                sb_persons.setProgress(persons)
            } else {
                if (!personMaxToast.getView().isShown) personMaxToast.show()
            }
            persons = persons
        })
        removePerson_button.setOnClickListener(View.OnClickListener {
            var persons = persons
            if (persons > 1) {
                persons--
                sb_persons.setProgress(persons)
            } else {
                if (!personMinToast.getView().isShown) personMinToast.show()
            }
            persons = persons
        })
        reset_button.setOnClickListener(View.OnClickListener {
            inputPanel!!.clear()
            if (bill_view.getError() != null) {
                bill_view.setError(null)
                bill_view.clearFocus()
            }
            bill_view.setText(BillFormatter.CURRENCY_SYMBOL + "0")
            tv_tip_amount.setText(BillFormatter.CURRENCY_SYMBOL + "0")
            tv_amount_person.setText(BillFormatter.CURRENCY_SYMBOL + "0")
            tv_total_amount.setText(BillFormatter.CURRENCY_SYMBOL + "0")
            sb_Tip.setProgress(15)
            sb_persons.setProgress(1)
            tv_tip_percent.setText("Tip 15%")
            tv_NoPersons.setText("People:1")
        })
        calc_button.setOnClickListener(View.OnClickListener {
            if (bill_view.getError() != null) {
                bill_view.setError(null)
                bill_view.clearFocus()
            }
            showResult(inputPanel)
        })
    }

    private fun showResult(inputPanel:InputPanel) {

        val sb_Tip = findViewById<SeekBar>(R.id.sb_Tip)
        val sb_persons = findViewById<SeekBar>(R.id.persons_bar)
        val tv_tip_amount = findViewById<TextView>(R.id.tv_tip_amount)
        val tv_total_amount = findViewById<TextView>(R.id.tv_total_amount)
        val tv_amount_person = findViewById<TextView>(R.id.tvAmountPerson)


        val strAmount = inputPanel!!.presentValue
        val tipPercent = sb_Tip!!.progress
        val NPerson = sb_persons!!.progress
        var billFormatter: BillFormatter? = null
        try {
            billFormatter = BillFormatter(strAmount, tipPercent, NPerson)
            tv_tip_amount!!.text = billFormatter.tipAmount
            tv_total_amount!!.text = billFormatter.totalAmount
            tv_amount_person!!.text = billFormatter.amountPerPerson
        } catch (e: ZeroAmountException) {
            inputPanel!!.zeroInput()
        }
    }

    private var persons: Int
        private get() = findViewById<SeekBar>(R.id.persons_bar).progress
        private set(persons) {
            val out = getPersonString(persons)
            val tv_NoPersons = findViewById<TextView>(R.id.NoPersons)
            tv_NoPersons!!.text = out
        }

    private var tip: Int
        private get() = findViewById<SeekBar>(R.id.sb_Tip).progress
        private set(tip) {
            val out = getTipString(tip)
            val tv_tip_percent = findViewById<TextView>(R.id.tvTipPercentage)
            tv_tip_percent!!.text = out
        }

}