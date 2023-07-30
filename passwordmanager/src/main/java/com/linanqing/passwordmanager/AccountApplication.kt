package com.linanqing.passwordmanager

import android.app.Application
import com.linanqing.passwordmanager.data.AppContainer
import com.linanqing.passwordmanager.data.AppDataContainer

class AccountApplication  : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}