package com.example.expensetrackerapp.di

import javax.inject.Qualifier


@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class DispatcherDefault

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class DispatcherIo

@Retention(AnnotationRetention.BINARY)
@Qualifier
internal annotation class DispatcherMain