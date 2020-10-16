package com.greenwallet.business.app

import android.content.Context

abstract class Application {

    companion object {

        lateinit var context: Context

        lateinit var app: android.app.Application
    }
}