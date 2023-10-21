package com.carhop.models

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val drivingScore: Int = 0,
    val userType: String = UserType.USER.userType

)

//limit user types to the following values
enum class UserType(val userType: String) {
    USER("user"),
    ADMIN("admin")
}