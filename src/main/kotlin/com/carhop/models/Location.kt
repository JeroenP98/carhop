package com.carhop.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String?,
    val address: String?
)