package com.indusbit.candor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<Button>(R.id.button).setOnClickListener {
            val myApplication = application as MyApplication
            val uuid = UUID.randomUUID().toString()
            val candor = myApplication.getInstance()!!

            candor.signOut()

            finish()
        }

    }
}
