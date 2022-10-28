package ru.spiridonov.be.kind.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.presentation.InvalidHelpActivity
import ru.spiridonov.be.kind.presentation.MainActivity
import ru.spiridonov.be.kind.presentation.VolunteerApproveHelpItemActivity
import ru.spiridonov.be.kind.presentation.VolunteerHelpListActivity
import ru.spiridonov.be.kind.presentation.account.AccountActivity
import ru.spiridonov.be.kind.presentation.account.UserProfileActivity
import ru.spiridonov.be.kind.presentation.account.ui.LoginFragment
import ru.spiridonov.be.kind.presentation.account.ui.RegisterFragment
import ru.spiridonov.be.kind.presentation.account.ui.VerifiedUserActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        //WorkerModule::class
    ]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: AccountActivity)
    fun inject(activity: UserProfileActivity)
    fun inject(activity: VerifiedUserActivity)
    fun inject(activity: InvalidHelpActivity)
    fun inject(activity: VolunteerHelpListActivity)
    fun inject(activity: VolunteerApproveHelpItemActivity)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
    fun inject(application: BeKindApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}