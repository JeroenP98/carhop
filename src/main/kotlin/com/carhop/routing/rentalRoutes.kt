package com.carhop.routing

import com.carhop.dao.rentals.rentalDao
import com.carhop.dto.rentals.RegisterRentalDTO
import com.carhop.entities.checkCarExists
import com.carhop.models.ResponseStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.*

fun Route.rentalRoutes() {
    authenticate("user") {
        route("cars/reserve") {
            post {
                //store new rental data, parsed from JSON into object
                val rentalRequest = call.receive<RegisterRentalDTO>()
                //retrieve the user id from JWT token
                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").toString()

                if (rentalRequest.renterId == userId.toInt()) {
                    if (checkCarExists(rentalRequest.carId)) {
                        if (rentalDao.checkCarAvailability(rentalRequest.carId)) {
                            if (rentalDao.checkTimeAvailability(rentalRequest.carId, rentalRequest.startDate, rentalRequest.endDate)) {
                                val rental = rentalDao.registerRental(rentalRequest)
                                if (rental != null) {
                                    call.respond(HttpStatusCode.OK, rental)
                                } else {
                                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Could not rent out car"))
                                }
                            } else {
                                call.respond(HttpStatusCode.Forbidden, ResponseStatus("Car is not available for rental in the given time slot"))
                            }
                        } else {
                            call.respond(HttpStatusCode.Forbidden, ResponseStatus("Car is not registered as available for rental"))
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("Car does not exist"))
                    }
                } else {
                    call.respond(HttpStatusCode.Unauthorized, ResponseStatus("Cannot make reservations for other users"))
                }

            }
        }

        route("cars/rental/{id}") {
            get {
                //retrieve id from url parameters
                val requestedRental = call.parameters["id"]
                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").toString().toInt()

                if (requestedRental != null) {
                    // retrieve the owner and renter listed for the rental and check if either of them matches id of JWT token
                    val owner = rentalDao.returnOwner(requestedRental.toInt())

                    val renter = rentalDao.returnRenter(requestedRental.toInt())


                    if (owner != null && renter != null) {
                        if (userId == owner.id || userId == renter.id) {
                            val rental = rentalDao.getRental(requestedRental.toInt())

                            if (rental != null) {
                                call.respond(HttpStatusCode.OK, rental)
                            } else {
                                call.respond(HttpStatusCode.NotFound, "Unable to retrieve rental")
                            }
                        } else {
                            call.respond(HttpStatusCode.Forbidden, ResponseStatus("Only allowed to check you own rentals. You are not listed as the car owner or renter"))
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("owner or renter not found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }


            }
            delete {
                //retrieve id from url parameters
                val requestedRental = call.parameters["id"]
                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("id").toString().toInt()

                if (requestedRental != null) {
                    // retrieve the owner and renter listed for the rental and check if either of them matches id of JWT token
                    val owner = rentalDao.returnOwner(requestedRental.toInt())
                    println(owner)
                    val renter = rentalDao.returnRenter(requestedRental.toInt())
                    println(renter)
                    if (owner != null && renter != null) {
                        if (userId == owner.id || userId == renter.id) {
                            val rental = rentalDao.getRental(requestedRental.toInt())
                            if (rental != null) {
                                if (rentalDao.checkIfRentalIsInPast(rental)) {
                                    rentalDao.deleteRental(rental.id)
                                    call.respond(HttpStatusCode.OK, ResponseStatus("Rental deleted"))
                                } else {
                                    call.respond(HttpStatusCode.Forbidden, ResponseStatus("Not allowed to delete past or ongoing rentals"))
                                }
                            } else {
                                call.respond(HttpStatusCode.NotFound, "Unable to retrieve rental")
                            }
                        } else {
                            call.respond(HttpStatusCode.Forbidden, ResponseStatus("Only allowed to delete you own rentals. You are not listed as the car owner or renter"))
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("owner or renter not found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("Invalid parameters"))
                }
            }
        }
    }
}