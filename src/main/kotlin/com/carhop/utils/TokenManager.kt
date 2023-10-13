package com.carhop.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.carhop.models.User
import java.util.*

class TokenManager {

    //Define token meta data
    private val jwtAudience = "jwt-audience"
    private val jwtDomain = "127.0.0.1:8080"
    private val jwtSecret = "secret"
    private val expirationDate = System.currentTimeMillis() + 24 * 60 * 60000 // 24 hours

    //generate token with user specific data
    fun generateJWTToken(user: User): String {

        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("email", user.email)
            .withClaim("id", user.id)
            .withClaim("userType", user.userType)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}