package com.carhop.dto.cars

import com.carhop.models.FuelType
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column

//variables needed for registering a new car
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
    val fuelType: FuelType,
    val transmission: String?
)
