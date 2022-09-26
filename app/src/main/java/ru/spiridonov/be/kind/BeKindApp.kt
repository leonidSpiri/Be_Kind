package ru.spiridonov.be.kind

import android.app.Application
import ru.spiridonov.be.kind.di.DaggerApplicationComponent

class BeKindApp : Application()/*, Configuration.Provider*/ {

    //@Inject
    //lateinit var workerFactory: CurrWorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    /*override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(
                workerFactory
            )
            .build()
    }*/
}