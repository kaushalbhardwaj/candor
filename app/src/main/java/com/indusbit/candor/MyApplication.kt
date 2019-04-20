package com.indusbit.candor

import android.app.Application
import com.indusbit.candorsdk.Candor

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
//        Candor.initialize(this, "kaushal")
    }

}