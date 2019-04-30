package com.indusbit.candor

import android.app.Application
import android.util.Log
import com.indusbit.candorsdk.Candor
import java.util.*

class MyApplication : Application() {

    private var candorInstance: Candor? = null

    override fun onCreate() {
        super.onCreate()
//        val uuid = UUID.randomUUID().toString()
//        Log.d("MyAPp", "UUID: $uuid")
        candorInstance = Candor.initialize(applicationContext, "a2886e1ffd2c0021671abc1ae45acd7f")
    }

    public fun getInstance(): Candor? {
        return candorInstance
    }
}