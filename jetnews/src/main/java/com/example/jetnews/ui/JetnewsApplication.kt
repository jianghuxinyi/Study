package com.example.jetnews.ui

import android.app.Application
import com.example.jetnews.data.AppContainer
import com.example.jetnews.data.AppContainerImpl

class JetnewsApplication : Application() {
    companion object {
        const val JETNEWS_APP_URI = "https://developer.android.com/jetnews"
    }

    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}