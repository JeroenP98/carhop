package com.carhop.models

import kotlinx.serialization.Serializable



@Serializable
data class Car (
    val id: Int,
    val ownerId: Int,
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
    val transmission: String?,
    val latitude: Double,
    val longitude: Double

)

enum class FuelType(val fuelType: String) {
    ELECTRIC("electric"),
    HYBRID("hybrid"),
    GASOLINE("gasoline")
}