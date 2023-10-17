package com.carhop.plugins

import com.carhop.routing.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        //apply routes used for user management
        userRoutes()
    }
}
