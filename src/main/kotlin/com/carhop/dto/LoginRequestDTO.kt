package com.carhop.dto

import kotlinx.serialization.Serializable

//used for receiving login requests
@Serializable
data class LoginRequestDTO (
    val email: String,
    val password: String
)