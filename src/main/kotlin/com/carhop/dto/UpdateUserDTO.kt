package com.carhop.dto

import kotlinx.serialization.Serializable

@Serializable
class UpdateUserDTO (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)