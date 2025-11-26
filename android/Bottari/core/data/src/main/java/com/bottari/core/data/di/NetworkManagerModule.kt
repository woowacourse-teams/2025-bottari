package com.bottari.core.data.di

import com.bottari.core.data.network.NetworkManagerImpl
import com.bottari.core.domain.network.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkManagerModule {
    @Binds
    @Singleton
    abstract fun bindNetworkManager(impl: NetworkManagerImpl): NetworkManager
}
