package com.carhop.routing

import com.carhop.dao.users.carDAO
import com.carhop.dto.RegisterCarDTO
import com.carhop.models.ResponseStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.carRoutes() {
    //create instance of the token manager to handle out tokens


    route("cars/register") {
        post {
            val car = call.receive<RegisterCarDTO>()


            if (true) {
                val newCar = carDAO.registerCar(car)
                if (newCar != null) {
                    call.respond(HttpStatusCode.OK)
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, ResponseStatus("Invalid Registration"))
            }
        }
    }
}