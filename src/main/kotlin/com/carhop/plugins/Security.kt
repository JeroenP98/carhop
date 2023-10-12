package com.carhop.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

val jwtAudience = "jwt-audience"
val jwtDomain = "127.0.0.1:8080"
val jwtRealm = "CarHop app"
val jwtSecret = "secret"

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain

    authentication {
        jwt("user") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)
                    && credential.payload.getClaim("userType").toString().lowercase() == "user") {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
        jwt("admin") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)
                    && credential.payload.getClaim("userType").toString().lowercase() == "admin") {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}


