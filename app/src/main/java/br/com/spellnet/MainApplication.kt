package br.com.spellnet

import android.app.Application
import br.com.spellnet.di.spellnetApp
import org.koin.standalone.StandAloneContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin(spellnetApp)
    }
}