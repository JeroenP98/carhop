package com.carhop.routing

import com.carhop.dao.cars.carDAO
import com.carhop.dto.cars.CarImageResponse
import com.carhop.dto.cars.RegisterCarDTO
import com.carhop.dto.cars.UpdateCarDTO
import com.carhop.entities.checkCarExists
import com.carhop.models.ResponseStatus
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.coobird.thumbnailator.Thumbnails
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

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
                        call.respond(HttpStatusCode.Forbidden, ResponseStatus("Unable to register car/ Licence plate already exists"))
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
                    val car = carDAO.getCarWithImage(requestedCar)
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

        route("cars/{id}/image") {
            post {
                val requestedCarId = call.parameters["id"]?.toIntOrNull()
                val imagesDir = File(javaClass.classLoader.getResource("images")!!.file)
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }

                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val fileBytes = part.streamProvider().readBytes()
                        val name = "car_${requestedCarId}_image"
                        val originalFile = File("${imagesDir}/${name}.jpg")
                        originalFile.writeBytes(fileBytes)


                        Thumbnails.of(originalFile)
                            .size(200, 200)
                            .toFile(originalFile)

                        call.respond(HttpStatusCode.OK, CarImageResponse("${BASE_URL}images/${name}.jpg"))

                    }
                    part.dispose()
                }

            }
            get {
                val requestedCarId = call.parameters["id"]?.toIntOrNull()
                if (requestedCarId != null) {
                    val name = "car_${requestedCarId}_image"
                    val imageUrl = "images/${name}.jpg"
                    call.respond(HttpStatusCode.OK, CarImageResponse("$BASE_URL$imageUrl"))
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
        route("cars/{id}/location") {
            get {
                val requestedCarId = call.parameters["id"]?.toIntOrNull()

                if (requestedCarId != null) {
                    val doesCarExist = checkCarExists(requestedCarId)
                    if (doesCarExist) {
                        val carLocation = carDAO.getCarLocation(requestedCarId)
                        if (carLocation != null) {
                            call.respond(HttpStatusCode.OK, carLocation)
                        } else {
                            call.respond(HttpStatusCode.BadRequest, "Could not retrieve location")
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

const val BASE_URL = "http://192.168.178.51:8080/"