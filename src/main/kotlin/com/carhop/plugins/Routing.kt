package com.carhop.plugins

import com.carhop.routing.adminRoutes
import com.carhop.routing.carRoutes
import com.carhop.routing.rentalRoutes
import com.carhop.routing.userRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File


fun Application.configureRouting() {
    routing {
        userRoutes()
        carRoutes()
        rentalRoutes()
        adminRoutes()
        staticFiles("/images", File(javaClass.classLoader.getResource("images")!!.file))

    }
}
