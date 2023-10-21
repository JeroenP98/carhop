package com.carhop.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Rental(
    val id: Int,
    val carId: Int,
    val renterId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val cost: Double
)