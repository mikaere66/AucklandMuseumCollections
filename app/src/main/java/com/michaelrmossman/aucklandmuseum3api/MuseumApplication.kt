package com.michaelrmossman.aucklandmuseum3api

import android.app.Application
import com.michaelrmossman.aucklandmuseum3api.data.AppContainer
import com.michaelrmossman.aucklandmuseum3api.data.DefaultAppContainer

class MuseumApplication: Application() {

    /* Auckland Museum Collections API key: "secrets.properties" */
    private val apiKey = BuildConfig.AM_API_KEY

    /* AppContainer instance, used for obtaining VM dependencies */
    lateinit var container: AppContainer

    companion object {

        lateinit var instance: MuseumApplication
    }

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer(
            apiKey, applicationContext
        )

        instance = this
    }
}