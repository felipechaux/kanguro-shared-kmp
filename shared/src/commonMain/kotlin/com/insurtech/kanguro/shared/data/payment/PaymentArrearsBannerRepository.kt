package com.insurtech.kanguro.shared.data.payment

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

interface PaymentArrearsBannerRepository {
    suspend fun saveLastUpdateInArrearsPayment(timestamp: Long)
    suspend fun getLastUpdateInArrearsPayment(): Long?
    fun getLastUpdateInArrearsPaymentFlow(): Flow<Long?>
    suspend fun shouldShowPaymentArrearsBanner(
        paymentArrears: PaymentArrearsUiModel,
        cooldownDurationMillis: Long = DEFAULT_COOLDOWN_DURATION_MILLIS
    ): Boolean

    companion object {
        const val DEFAULT_COOLDOWN_DURATION_MILLIS = 24 * 60 * 60 * 1000L
    }
}

class PaymentArrearsBannerDataStoreRepository(
    private val dataStore: DataStore<Preferences>
) : PaymentArrearsBannerRepository {

    companion object {
        private val LAST_UPDATE_IN_ARREARS_PAYMENT_KEY = longPreferencesKey("last_update_in_arrears_payment")
    }

    override suspend fun saveLastUpdateInArrearsPayment(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[LAST_UPDATE_IN_ARREARS_PAYMENT_KEY] = timestamp
        }
    }

    override suspend fun getLastUpdateInArrearsPayment(): Long? {
        return dataStore.data.first()[LAST_UPDATE_IN_ARREARS_PAYMENT_KEY]
    }

    override fun getLastUpdateInArrearsPaymentFlow(): Flow<Long?> {
        return dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_IN_ARREARS_PAYMENT_KEY]
        }
    }

    override suspend fun shouldShowPaymentArrearsBanner(
        paymentArrears: PaymentArrearsUiModel,
        cooldownDurationMillis: Long
    ): Boolean {
        if (!paymentArrears.shouldShow) {
            return false
        }

        val lastUpdateTimestamp = getLastUpdateInArrearsPayment()
        
        if (lastUpdateTimestamp == null) {
            return true
        }

        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        val timeDifference = currentTimestamp - lastUpdateTimestamp

        // Show banner if cooldown period has passed
        return timeDifference >= cooldownDurationMillis
    }
}