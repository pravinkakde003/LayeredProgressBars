package com.user.layeredprogressbars

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> if (outerProgressTotalEditText!!.text != null && outerProgressTotalEditText!!.text.isNotEmpty() && outerProgressOutOfEditText!!.text != null && outerProgressOutOfEditText!!.text.isNotEmpty() && innerProgressTotalEditText!!.text != null && innerProgressTotalEditText!!.text.isNotEmpty() && innerProgressOutOfEditText!!.text != null && innerProgressOutOfEditText!!.text.isNotEmpty()) {
                val outerProgressBarValue = calculatePercentage(outerProgressOutOfEditText!!.text.toString().toFloat(), outerProgressTotalEditText!!.text.toString().toFloat())
                val innerProgressBarValue = calculatePercentage(innerProgressOutOfEditText!!.text.toString().toFloat(), innerProgressTotalEditText!!.text.toString().toFloat())
                layeredProgressBars!!.setInnerProgressBarProgress(innerProgressBarValue, true)
                layeredProgressBars!!.setOuterProgressBarProgress(outerProgressBarValue, true)

//                SET CUSTOM VALUE BY METHODS
//                layeredProgressBars.setOuterProgressBarColor("#F6780D", true)
//                layeredProgressBars.setInnerProgressBarColor("#F6780D", true)
//                layeredProgressBars.setProgressBarUnfinishedColor("#000000", true)
//                layeredProgressBars.setProgressBarDefaultFilledColor("#000000", true)
            } else {
                Toast.makeText(this, "Enter all values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculatePercentage(obtained: Float, total: Float): Float {
        return (obtained * 100 / total)
    }
}