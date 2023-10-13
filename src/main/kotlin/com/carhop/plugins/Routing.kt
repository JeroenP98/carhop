package com.carhop.plugins

import com.carhop.routing.carRoutes
import com.carhop.routing.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRoutes()
        carRoutes()
    }
}
