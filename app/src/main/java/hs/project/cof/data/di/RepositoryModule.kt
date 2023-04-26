package hs.project.cof.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hs.project.cof.data.repository.ChatServiceRepository
import hs.project.cof.data.repository.ChatServiceRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindChatServiceRepository(
        chatServiceRepositoryImpl: ChatServiceRepositoryImpl,
    ): ChatServiceRepository
}