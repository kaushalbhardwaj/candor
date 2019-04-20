package com.indusbit.candor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.indusbit.candorsdk.Candor
import com.indusbit.candorsdk.Variant

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Candor.initialize(applicationContext, "kaushal")

        val variant = Candor.getExperiment("btn_colour")

        if(variant != null)
            Log.d("mainActivity", variant.key)
        val button = findViewById<Button>(R.id.button);
        button.setOnClickListener {
            val intent = Intent(applicationContext, Main2Activity::class.java)
            startActivity(intent)
        }

    }
}
