package com.aroman.testexcercise5

import android.app.Application
import com.aroman.testexcercise5.di.KoinModules.repository
import com.aroman.testexcercise5.di.KoinModules.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(repository, viewModel))
        }
    }
}