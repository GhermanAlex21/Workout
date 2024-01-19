package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "VIZUALIZARE_UNITATI_METRICE"
    val US_UNITS_VIEW = "VIZUALIZARE_UNITATI_US"

    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)
        val etUSUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etUSUnitWeight)
        val etUSUnitHeightFeet =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightFeet)
        val etUSUnitHeightInch =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightInch)
        val toolbar_bmi_activity =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_bmi_activity)
        setSupportActionBar(toolbar_bmi_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULEAZĂ BMI"
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val btnCalculateUnits = findViewById<Button>(R.id.btnCalculateUnits)
        val etMetricUnitWeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitWeight)
        val etMetricUnitHeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitHeight)
        val rgUnits = findViewById<RadioGroup>(R.id.rgUnits)
        btnCalculateUnits.setOnClickListener {
            if (currentVisibleView.equals(METRIC_UNITS_VIEW)) {
                if (validateMetricUnits()) {
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)

                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Vă rugăm să introduceți valori valide.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            } else {
                if (validateUSUnits()) {
                    val usUnitHeightValueFeet: String = etUSUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch: String = etUSUnitHeightInch.text.toString()
                    val usUnitWeightValue: Float = etUSUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)

                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Vă rugăm să introduceți valori valide.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbMetricUnits)
                makeVisibleMetricUnitsView()
            else
                makeVisibleUSUnitsView()
        }

    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        val tilMetricUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilMetricUnitWeight)
        val tilMetricUnitHeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilMetricUnitHeight)
        val tilUSUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilUSUnitWeight)
        val llUSUnitHeight = findViewById<LinearLayout>(R.id.llUsUnitHeight)
        val llDisplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)
        val etMetricUnitHeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitHeight)
        val etMetricUnitWeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitWeight)
        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE
        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()
        tilUSUnitWeight.visibility = View.GONE
        llUSUnitHeight.visibility = View.GONE
        llDisplayBMIResult.visibility = View.INVISIBLE

    }

    private fun makeVisibleUSUnitsView() {
        currentVisibleView = US_UNITS_VIEW
        val tilMetricUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilMetricUnitWeight)
        val tilMetricUnitHeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilMetricUnitHeight)
        val tilUSUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilUSUnitWeight)
        val llUSUnitHeight = findViewById<LinearLayout>(R.id.llUsUnitHeight)
        val llDisplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)
        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE
        val etUSUnitWeight =
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etUSUnitWeight)
        val etUSUnitHeightFeet =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightFeet)
        val etUSUnitHeightInch =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightInch)
        etUSUnitHeightFeet.text!!.clear()
        etUSUnitWeight.text!!.clear()
        etUSUnitHeightInch.text!!.clear()

        tilUSUnitWeight.visibility = View.VISIBLE
        llUSUnitHeight.visibility = View.VISIBLE
        llDisplayBMIResult.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String
        val tvBMIValue = findViewById<TextView>(R.id.tvBMIValue)
        val tvBMIType = findViewById<TextView>(R.id.tvBMIType)
        val tvBMIDescription = findViewById<TextView>(R.id.tvBMIDescription)
        val tvYourBMI = findViewById<TextView>(R.id.tvYourBMI)
        val llDisplayBMIResult = findViewById<LinearLayout>(R.id.llDiplayBMIResult)


        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Subponderal foarte sever"
            bmiDescription =
                "Oops! Aveți nevoie cu adevărat să aveți mai multă grijă de voi înșivă! Mâncați mai mult!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Subponderal sever"
            bmiDescription =
                "Oops! Aveți nevoie cu adevărat să aveți mai multă grijă de voi înșivă! Mâncați mai mult!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Subponderal"
            bmiDescription =
                "Oops! Aveți nevoie cu adevărat să aveți mai multă grijă de voi înșivă! Mâncați mai mult!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Felicitări! Vă aflați într-o formă bună!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Supraponderal"
            bmiDescription =
                "Oops! Aveți nevoie cu adevărat să aveți grijă de voi înșivă! Poate să începeți să faceți exerciții!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obezitate Clasa I (Moderat obez)"
            bmiDescription =
                "Oops! Aveți nevoie cu adevărat să aveți grijă de voi înșivă! Poate să începeți să faceți exerciții!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obezitate Clasa II (Sever obez)"
            bmiDescription =
                " Vă aflați într-o condiție foarte periculoasă! Acționați acum!"
        } else {
            bmiLabel = "Obezitate Clasa III (Obezitate foarte severă)"
            bmiDescription =
                " Vă aflați într-o condiție foarte periculoasă! Acționați acum!"
        }


        llDisplayBMIResult.visibility = View.VISIBLE


        val bmiValue =
            BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean {

        var isValid = true
        val etMetricUnitWeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitWeight)
        val etMetricUnitHeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etMetricUnitHeight)
        if (etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if (etMetricUnitHeight.text.toString().isEmpty())
            isValid = false
        return isValid
    }

    private fun validateUSUnits(): Boolean {

        var isValid = true
        val etUSUnitHeightFeet =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightFeet)
        val etUSUnitHeightInch =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitHeightInch)
        val etUSUnitWeight =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.etUSUnitWeight)
        when {
            etUSUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            etUSUnitHeightInch.text.toString().isEmpty() -> isValid = false
            etUSUnitWeight.text.toString().isEmpty() -> isValid = false
        }
        return isValid
    }
}
