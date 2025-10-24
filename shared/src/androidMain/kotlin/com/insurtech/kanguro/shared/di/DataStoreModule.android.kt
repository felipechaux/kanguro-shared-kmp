package com.insurtech.kanguro.shared.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.insurtech.kanguro.shared.Constants
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun dataStoreModule(): Module = module {
    single<DataStore<Preferences>> {
        createDataStore(androidContext())
    }
}

fun createDataStore(context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { context.filesDir.resolve(Constants.DATA_STORE_FILE_NAME).absolutePath.toPath() }
    )
}
