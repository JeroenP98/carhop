package com.carhop.routing

import com.carhop.dao.cars.carDAO
import com.carhop.dao.rentals.rentalDao
import com.carhop.dao.users.userDAO
import com.carhop.dto.users.LoginRequestDTO
import com.carhop.dto.users.LoginResponse
import com.carhop.dto.users.RegisterUserDto
import com.carhop.dto.users.UpdateUserDTO
import com.carhop.models.ResponseStatus
import com.carhop.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*




fun Route.userRoutes() {

    //create instance of the token manager to handle out tokens
    val tokenManager = TokenManager()

    route("users/register") {
        post {
            val user = call.receive<RegisterUserDto>()

            //regex for validating user email input
            val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            //val passwordRegex = Regex("") password regex disabled for testing purposes

            if (emailPattern.matches(user.email)) {
                val newUser = userDAO.registerUser(user)

                if (newUser != null) {
                    //create token if user was created
                    val jwtToken = tokenManager.generateJWTToken(newUser)
                    call.respond(HttpStatusCode.OK, LoginResponse(jwtToken, newUser.id))
                }
                else {
                    call.respond(HttpStatusCode.Forbidden, ResponseStatus("User/Email already exists"))
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, ResponseStatus("Invalid email"))
            }
        }
    }

    route("users/login") {
        post {
            val loginRequest = call.receive<LoginRequestDTO>()
            val isLoginRequestValid = userDAO.loginUser(loginRequest)

            if (isLoginRequestValid != null) {
                val jwtToken = tokenManager.generateJWTToken(isLoginRequestValid)
                call.respond(HttpStatusCode.OK, LoginResponse(jwtToken, isLoginRequestValid.id))
            } else {
                call.respond(HttpStatusCode.Forbidden, ResponseStatus("Invalid email or password"))
            }
        }
    }

    authenticate("user") {
        route("users/profile/{id}") {
            get {
                //retrieve path parameter
                val requestedId = call.parameters["id"]

                //retrieve JWT token from HTTP header
                val token = call.principal<JWTPrincipal>()
                //retrieve the user id value stored in JWT token
                val userId = token!!.payload.getClaim("id").toString()

                //check if user id matches the id in the parameters. users can only change their own details
                if (requestedId == userId) {
                    val userDetails = userDAO.getUser(userId.toInt())
                    if (userDetails != null) {
                        call.respond(HttpStatusCode.OK, userDetails)
                    }
                } else {
                    call.respond(HttpStatusCode.Forbidden, ResponseStatus("Can only request to see your own account"))
                }
            }


            put {
                val requestedId = call.parameters["id"]
                val newUserValues = call.receive<UpdateUserDTO>()
                //retrieve JWT token from HTTP header
                val token = call.principal<JWTPrincipal>()
                //retrieve the user id value stored in JWT token
                val userId = token!!.payload.getClaim("id").toString()

                //check if user id matches the id in the parameters. users can only change their own details
                if (requestedId == userId) {
                    val updatedUser = userDAO.updateUser(newUserValues, requestedId.toInt())
                    if (updatedUser != null) {
                        call.respond(HttpStatusCode.OK, updatedUser)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, ResponseStatus("Unable to update user"))
                    }
                } else {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        ResponseStatus("Not allowed to change another profile but your own")
                    )
                }
            }

            delete {
                val requestedId = call.parameters["id"]
                val token = call.principal<JWTPrincipal>()
                //retrieve the user id value stored in JWT token
                val userId = token!!.payload.getClaim("id").toString()

                //check if user id matches the id in the parameters. users can only change their own details
                if (requestedId == userId) {
                    userDAO.deleteUser(requestedId.toInt())
                    call.respond(HttpStatusCode.OK, ResponseStatus("Account deleted"))
                } else {
                    call.respond(
                        HttpStatusCode.Forbidden,
                        ResponseStatus("Not allowed to change another profile but your own")
                    )
                }
            }


        }
        route("users/cars/{id}") {
            get {
                val requestedId = call.parameters["id"]?.toIntOrNull()

                if (requestedId != null) {
                    val carList = carDAO.getAllUserCars(requestedId)
                    if (carList != null) {
                        call.respond(HttpStatusCode.OK, carList)
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseStatus("No cars found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("invalid parameters"))
                }
            }
        }
        route("users/rentals/{id}") {
            get {
                val requestedId = call.parameters["id"]?.toIntOrNull()

                if (requestedId != null) {
                    val rentalList = rentalDao.getAllUserRentals(requestedId)
                    if (rentalList != null) {
                        call.respond(HttpStatusCode.OK, rentalList)
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseStatus("No rentals found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("invalid parameters"))
                }
            }
        }
        route("users/rented/{id}") {
            get {
                val requestedId = call.parameters["id"]?.toIntOrNull()

                if (requestedId != null) {
                    val rentalList = rentalDao.getAllUserRentedOutCars(requestedId)
                    if (rentalList != null) {
                        call.respond(HttpStatusCode.OK, rentalList)
                    } else {
                        call.respond(HttpStatusCode.OK, ResponseStatus("No rentals found"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, ResponseStatus("invalid parameters"))
                }
            }
        }

    }

}