package com.carhop.dto

import kotlinx.serialization.Serializable
@Serializable
data class UpdateCarDTO (
    val licensePlate: String,
    val rentalPrice: Double,
    val available: Boolean,
    val brandName: String,
    val modelName: String,
    val buildYear: Int,
    val numOfSeats: Int,
    val emissionCategory: Char,
    val purchasePrice: Double,
    val monthlyInsuranceCost: Double,
    val yearlyMaintenanceCost: Double,
    val range: Double?,
    val fuelType: String?,
    val transmission: String?

)

