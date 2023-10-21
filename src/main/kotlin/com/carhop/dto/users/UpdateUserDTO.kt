package com.carhop.dto.users

import kotlinx.serialization.Serializable

//variables needed to update existing user
@Serializable
class UpdateUserDTO (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)