package com.greenwallet.business.app

import android.app.Application
import android.content.Context

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this
        context = this.applicationContext
    }

    companion object {

        lateinit var context: Context
            private set

        lateinit var app: Application
            private set
    }
}