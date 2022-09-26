package ru.spiridonov.be.kind.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.presentation.MainActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        // ViewModelModule::class,
        //WorkerModule::class
    ]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    //fun inject(fragment: CurrDetailFragment)
    fun inject(application: BeKindApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}