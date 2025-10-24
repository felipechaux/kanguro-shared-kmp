package com.insurtech.kanguro.shared.di

import android.content.Context
import com.insurtech.kanguro.shared.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoinAndroid(
    context: Context,
    kanguroAccessToken: String,
    kanguroRefreshToken: String,
    kanguroHost: String,
    kanguroApiKey: String,
    questSystemHost: String,
    questSystemAuthToken: String,
    config: KoinAppDeclaration? = null
) {
    startKoin {
        androidContext(context)
        config?.invoke(this)
        properties(
            mapOf(
                Constants.PROPERTY_QUEST_SYSTEM_HOST to questSystemHost,
                Constants.PROPERTY_QUEST_SYSTEM_AUTH_TOKEN to questSystemAuthToken,
                Constants.PROPERTY_KANGURO_ACCESS_TOKEN to kanguroAccessToken,
                Constants.PROPERTY_KANGURO_REFRESH_TOKEN to kanguroRefreshToken,
                Constants.PROPERTY_KANGURO_HOST to kanguroHost,
                Constants.PROPERTY_KANGURO_API_KEY to kanguroApiKey
            )
        )
        modules(commonDataStoreModule, profileModule, questSystemModule, claimsModule, pharmacyModule, paymentArrearsBannerModule)
    }
}
