package ru.spiridonov.be.kind.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.spiridonov.be.kind.presentation.account.AccountViewModel
import ru.spiridonov.be.kind.presentation.active_work.ActiveHelpViewModel
import ru.spiridonov.be.kind.presentation.create_work.HelpViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.MainViewModel

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel::class)
    fun bindHelpViewModel(viewModel: HelpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActiveHelpViewModel::class)
    fun bindActiveHelpViewModel(viewModel: ActiveHelpViewModel): ViewModel
}