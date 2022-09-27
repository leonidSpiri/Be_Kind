package ru.spiridonov.be.kind.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.spiridonov.be.kind.presentation.viewmodels.RegisterViewModel

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun bindMainViewModel(viewModel: RegisterViewModel): ViewModel
}