package br.com.spellnet

import android.app.Application
import br.com.spellnet.di.spellnetApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            // Android context
            androidContext(this@MainApplication)
            // modules
            modules(spellnetApp)
        }
    }
}