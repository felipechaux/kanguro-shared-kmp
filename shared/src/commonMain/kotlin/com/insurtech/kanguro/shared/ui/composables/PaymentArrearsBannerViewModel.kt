package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.runtime.Composable
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsType
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsUiModel
import com.insurtech.kanguro.shared.sharingresources.MR
import dev.icerock.moko.resources.compose.stringResource

class PaymentArrearsBannerViewModel {

    @Composable
    fun getMainMessage(paymentArrears: PaymentArrearsUiModel): String {
        return when (paymentArrears.type) {
            PaymentArrearsType.PET -> {
                getPetMessage(paymentArrears)
            }
            PaymentArrearsType.RENTERS -> {
                getRentersMessage(paymentArrears)
            }
            PaymentArrearsType.BOTH -> {
                getBothMessage()
            }
        }
    }

    @Composable
    fun getAmountMessage(paymentArrears: PaymentArrearsUiModel): String {
        return stringResource(MR.strings.amount_due, paymentArrears.amountDue)
    }

    @Composable
    private fun getPetMessage(paymentArrears: PaymentArrearsUiModel): String {
        val petNames = formatPetNames(paymentArrears.policies)
        val days = paymentArrears.daysInArrears

        return if (paymentArrears.policies.size == 1) {
            stringResource(MR.strings.pet_single_payment_overdue, petNames)
        } else {
            stringResource(MR.strings.pet_multiple_payments_overdue, petNames, days)
        }
    }

    @Composable
    private fun getRentersMessage(paymentArrears: PaymentArrearsUiModel): String {
        val dwellingType = paymentArrears.dwellingType
        return stringResource(MR.strings.renters_payment_overdue, dwellingType.toString())
    }

    @Composable
    private fun getBothMessage(): String {
        return stringResource(MR.strings.both_coverages_payment_overdue)
    }

    @Composable
    private fun formatPetNames(petNames: List<String>): String {
        return when {
            petNames.isEmpty() -> ""
            petNames.size == 1 -> petNames.first()
            petNames.size == 2 -> {
                val andWord = stringResource(MR.strings.conjunction_and)
                "${petNames[0]} $andWord ${petNames[1]}"
            }
            else -> {
                val andWord = stringResource(MR.strings.conjunction_and)
                val lastPet = petNames.last()
                val otherPets = petNames.dropLast(1).joinToString(", ")
                "$otherPets $andWord $lastPet"
            }
        }
    }
}
