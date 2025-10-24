package com.insurtech.kanguro.shared.di

import com.insurtech.kanguro.shared.data.payment.PaymentArrearsBannerDataStoreRepository
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsBannerRepository
import com.insurtech.kanguro.shared.ui.composables.PaymentArrearsBannerVisibilityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val paymentArrearsBannerModule = module {
    single<PaymentArrearsBannerRepository> {
        PaymentArrearsBannerDataStoreRepository(get())
    }
    
    viewModel {
        PaymentArrearsBannerVisibilityViewModel()
    }
}