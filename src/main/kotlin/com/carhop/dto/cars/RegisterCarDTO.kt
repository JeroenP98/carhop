package com.carhop.dto.cars

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column

@Serializable
data class RegisterCarDTO (
    val UserId: Int,
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