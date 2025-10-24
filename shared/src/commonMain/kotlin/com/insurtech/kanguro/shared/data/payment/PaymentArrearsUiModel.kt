package com.insurtech.kanguro.shared.data.payment

/**
 * UI model for payment arrears information.
 *
 * For BOTH type:
 * - amountDue should contain the sum of pet and renters amounts
 * - daysInArrears should contain the maximum days in arrears between pet and renters
 * - policies should contain both pet names and renters location info
 * - petAmountDue, rentersAmountDue, petDaysInArrears, rentersDaysInArrears are used for detailed information
 */
data class PaymentArrearsUiModel(
    val type: PaymentArrearsType,
    val policies: List<String>, // Pet names for pet policies, address info for renters
    val daysInArrears: Int,
    val amountDue: String,
    val shouldShow: Boolean = false,
    val dwellingType: String? = null
)
