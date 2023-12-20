package com.carhop.models

import kotlinx.serialization.Serializable

@Serializable
data class CarLocation(
    val carId: Int,
    val latitude: Double,
    val longitude: Double,

)
