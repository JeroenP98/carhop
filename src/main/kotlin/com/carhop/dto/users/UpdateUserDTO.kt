package com.carhop.dto.users

import kotlinx.serialization.Serializable

@Serializable
class UpdateUserDTO (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)