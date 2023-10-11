package com.carhop.dto

import com.carhop.models.UserType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
class RegisterUserDto (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    @EncodeDefault val drivingScore: Int = 0,
    @EncodeDefault val userType: String = UserType.USER.userType
)
