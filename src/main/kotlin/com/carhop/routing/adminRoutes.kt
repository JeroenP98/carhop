package com.carhop.routing

import com.carhop.dao.cars.carDAO
import com.carhop.dao.rentals.rentalDao
import com.carhop.dao.users.userDAO
import com.carhop.models.ResponseStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes() {
    authenticate("admin") {
        route("admin/users") {
            get {
                call.respond(HttpStatusCode.OK, userDAO.getAllUsers())
            }
        }

        route("admin/cars") {
            get {
                call.respond(HttpStatusCode.OK, carDAO.getAllCars())
            }
        }

        route("admin/rentals") {
            get {
                call.respond(HttpStatusCode.OK, rentalDao.getAllRentals())
            }
        }

        route("admin/users/{id}") {
            delete {
                val requestedId = call.parameters["id"]?.toIntOrNull()
                if (requestedId != null) {
                    userDAO.deleteUser(requestedId)
                    call.respond(HttpStatusCode.OK, ResponseStatus("Account deleted"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }
            }

        }

        route("admin/cars/{id}") {
            delete {
                val requestedId = call.parameters["id"]?.toIntOrNull()
                if (requestedId != null) {
                    call.respond(HttpStatusCode.OK, ResponseStatus("Account deleted"))
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }
            }

        }
    }
}