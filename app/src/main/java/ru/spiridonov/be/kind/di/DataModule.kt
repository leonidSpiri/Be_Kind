package ru.spiridonov.be.kind.di

import dagger.Binds
import dagger.Module
import ru.spiridonov.be.kind.data.repository.WorkListRepositoryImpl
import ru.spiridonov.be.kind.domain.repository.WorkListRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCurrencyRepository(impl: WorkListRepositoryImpl): WorkListRepository

    /* companion object {
         @Provides
         @ApplicationScope
         fun provideCurrListDao(
             application: Application
         ): CurrListDao {
             return AppDatabase.getInstance(application).currListDao()
         }
     }*/
}