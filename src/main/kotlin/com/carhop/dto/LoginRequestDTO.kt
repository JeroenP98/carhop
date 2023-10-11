package com.carhop.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO (
    val email: String,
    val password: String
)