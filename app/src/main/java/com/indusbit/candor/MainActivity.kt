package com.indusbit.candor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.indusbit.candorsdk.Candor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myApplication = application as MyApplication
        val candor = myApplication.getInstance()

        val experiment = candor!!.getExperiment("btn_colour")

        if (experiment != null) {
            findViewById<TextView>(R.id.variant_key).text = experiment.variant.key
        } else {
            findViewById<TextView>(R.id.variant_key).text = "default logic"
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(applicationContext, Main2Activity::class.java)
            startActivity(intent)
        }

    }
}
