package com.indusbit.candor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.indusbit.candorsdk.Candor
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var candor: Candor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myApplication = application as MyApplication
        val uuid = UUID.randomUUID().toString()
        candor = myApplication.getInstance()!!

        candor!!.signIn(uuid)
        val experiment = candor!!.getExperiment("btn_colour")

        val properties = JSONObject()
        properties.put("asdf", "sadf")
        properties.put("asdf", "sadf")

//        candor.track("name", properties)

        if (experiment != null)
            findViewById<TextView>(R.id.last_value).text = experiment.variant.key
        else
            findViewById<TextView>(R.id.last_value).text = "default last value"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
//            candor.track("name", properties)

            val intent = Intent(applicationContext, Main2Activity::class.java)
            startActivity(intent)
        }

        candor.setExperimentFetchedListener {

            this.runOnUiThread(Runnable {
                findViewById<TextView>(R.id.last_value).text = it.experiments.size.toString()
                Toast.makeText(applicationContext, "experiments fetched", Toast.LENGTH_SHORT).show()
            })


            Log.d("mainActivity", it.toString())
        }

    }

    override fun onResume() {
        super.onResume()

        val experiment = candor!!.getExperiment("btn_colour")



        if (experiment != null) {

            findViewById<TextView>(R.id.variant_key).text = experiment.variant.key
        } else {
            findViewById<TextView>(R.id.variant_key).text = "default logic"
        }
    }
}
