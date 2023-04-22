package com.example.sampleapplication.core.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module()
object AppModule {

    @Provides
    fun provideString(): String {
        return "Hello World"
    }

}