package com.carhop.dto.users

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val jwtToken: String,
    val userId: Int
)
