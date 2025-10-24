package com.insurtech.kanguro.shared.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun dataStoreModule(): Module

val commonDataStoreModule = module {
    includes(dataStoreModule())
}
