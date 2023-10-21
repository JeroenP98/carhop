package com.carhop.dto.users

import com.carhop.models.UserType
import kotlinx.serialization.Serializable

//used for receiving user registration requests
@Serializable
data class RegisterUserDto (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val drivingScore: Int = 0,
    val userType: String = UserType.USER.userType
)
