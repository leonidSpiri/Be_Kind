package ru.spiridonov.be.kind.di

import dagger.Binds
import dagger.Module
import ru.spiridonov.be.kind.data.repository.AccountRepositoryImpl
import ru.spiridonov.be.kind.data.repository.InvalidItemRepositoryImpl
import ru.spiridonov.be.kind.data.repository.VolunteerItemRepositoryImpl
import ru.spiridonov.be.kind.data.repository.WorkListRepositoryImpl
import ru.spiridonov.be.kind.domain.repository.AccountRepository
import ru.spiridonov.be.kind.domain.repository.InvalidItemRepository
import ru.spiridonov.be.kind.domain.repository.VolunteerItemRepository
import ru.spiridonov.be.kind.domain.repository.WorkListRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindWorkListRepository(impl: WorkListRepositoryImpl): WorkListRepository

    @Binds
    @ApplicationScope
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @ApplicationScope
    fun bindInvalidItemRepository(impl: InvalidItemRepositoryImpl): InvalidItemRepository

    @Binds
    @ApplicationScope
    fun bindVolunteerItemRepository(impl: VolunteerItemRepositoryImpl): VolunteerItemRepository

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