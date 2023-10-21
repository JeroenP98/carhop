package com.carhop.dto.rentals

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

//values needed to register a new rental
@Serializable
data class RegisterRentalDTO (
    val carId: Int,
    val renterId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val cost: Double
)