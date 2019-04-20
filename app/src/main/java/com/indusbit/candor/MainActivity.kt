package com.indusbit.candor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.indusbit.candorsdk.Candor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Candor.initialize(applicationContext, "kaushal")

        val variant = Candor.getExperiment("btn_colour")

        if (variant != null)
            Log.d("mainActivity", variant.key)
        val button = findViewById<Button>(R.id.button);
        button.setOnClickListener {
            val intent = Intent(applicationContext, Main2Activity::class.java)
            startActivity(intent)
        }

    }
}
