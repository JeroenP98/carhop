package com.carhop.dto

import com.carhop.models.UserType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val drivingScore: Int = 0,
    val userType: String = UserType.USER.userType
)
