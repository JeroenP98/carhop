package com.carhop.routing

import com.carhop.dao.cars.carDAO
import com.carhop.dto.cars.RegisterCarDTO
import com.carhop.dto.cars.UpdateCarDTO
import com.carhop.entities.checkCarExists
import com.carhop.models.ResponseStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.carRoutes() {
    authenticate("user") {
        route("cars/register") {
            post {
                val car = call.receive<RegisterCarDTO>()

                //retrieve JWT token from HTTP header
                val token = call.principal<JWTPrincipal>()
                //retrieve the user id value stored in JWT token
                val userId = token!!.payload.getClaim("id").toString()

                if (userId.toInt() == car.UserId) {
                    val newCar = carDAO.registerCar(car)

                    if (newCar != null) {
                        call.respond(HttpStatusCode.OK, mapOf("car" to newCar))
                    } else  {
                        call.respond(HttpStatusCode.Forbidden, ResponseStatus("Unable to register car/ Lisence plate already exists"))
                    }

                } else {
                    call.respond(HttpStatusCode.Forbidden, ResponseStatus("Cannot register a car for a different user"))
                }
            }
        }

        route("cars/search") {
            get {
                val carList = carDAO.getAllCars()

                call.respond(HttpStatusCode.OK, carList)
            }
        }

        route("cars/{id}") {
            get {
                val requestedCar = call.parameters["id"]?.toIntOrNull()

                if (requestedCar != null) {
                    val car = carDAO.getCar(requestedCar)
                    if (car != null) {
                        call.respond(HttpStatusCode.OK, car)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("Unable to find car"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }
            }

            put {
                val newCarValues = call.receive<UpdateCarDTO>()

                val requestedId = call.parameters["id"]
                //retrieve JWT token from HTTP header

                if (requestedId != null) {
                    val carBelongsTo = carDAO.getOwnerIdByCarId(requestedId.toInt())

                    val token = call.principal<JWTPrincipal>()
                    //retrieve the user id value stored in JWT token
                    val userId = token!!.payload.getClaim("id").toString()

                    if (userId.toInt() == carBelongsTo) {
                        val updatedCar = carDAO.updateCar(newCarValues)

                        if (updatedCar != null) {
                            call.respond(HttpStatusCode.OK, updatedCar)
                        } else {
                            call.respond(HttpStatusCode.BadRequest, ResponseStatus("Unable to update car"))
                        }

                    } else {
                        call.respond(HttpStatusCode.Forbidden, ResponseStatus("Not allowed to change any other car but your own"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }




            }

            delete {
                val requestedId = call.parameters["id"]
                //retrieve JWT token from HTTP header
                val token = call.principal<JWTPrincipal>()
                //retrieve the user id value stored in JWT token
                val userId = token!!.payload.getClaim("id").toString()

                if (requestedId != null) {
                    val carBelongsTo = carDAO.getOwnerIdByCarId(requestedId.toInt())

                    if (carBelongsTo == userId.toInt()) {

                        // TODO: implement logic to deny users from deleting cars when there are still active rentals
                        carDAO.deleteCar(requestedId.toInt())
                        call.respond(HttpStatusCode.OK, ResponseStatus("Car deleted"))
                    } else {
                        call.respond(HttpStatusCode.Forbidden, ResponseStatus("Not allowed to delete any other car but your own"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }


            }
        }

        route("cars/{id}/tco") {
            get {
                val requestedCarId = call.parameters["id"]?.toIntOrNull()

                if (requestedCarId != null) {
                    val doesCarExist = checkCarExists(requestedCarId)
                    if (doesCarExist) {
                        val tco = carDAO.getTotalCostOfOwnership(requestedCarId)
                        if (tco != null) {
                            call.respond(HttpStatusCode.OK, mapOf("TCO" to tco))
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Could not calculate TCO")
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("Car id not found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("invalid parameters"))
                }
            }
        }
    }

}