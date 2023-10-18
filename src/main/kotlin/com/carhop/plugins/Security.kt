package com.carhop.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

//configure variables used to sign the JWT token
val jwtAudience = "jwt-audience"
val jwtDomain = "127.0.0.1:8080"
val jwtRealm = "CarHop app"
val jwtSecret = "secret"

fun Application.configureSecurity() {
    authentication {
        //authentication to be used for pages only accessible by users
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
                    && credential.payload.getClaim("userType").asString().lowercase() == "user") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        //authentication to be used for pages only accessible by admins
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
                    && credential.payload.getClaim("userType").asString().lowercase() == "admin") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}


