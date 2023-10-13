package com.carhop.routing

import com.carhop.dao.users.userDAO
import com.carhop.dto.RegisterUserDto
import com.carhop.models.ResponseStatus
import com.carhop.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Route.userRoutes() {

    //create instance of the token manager to handle out tokens
    val tokenManager = TokenManager()

    route("users/register") {
        post {
            val user = call.receive<RegisterUserDto>()
            val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
            //val passwordRegex = Regex("") password regex disabled for testing purposes
            if (emailPattern.matches(user.email)) {
                val newUser = userDAO.registerUser(user)
                if (newUser != null) {
                    val jwtToken = tokenManager.generateJWTToken(newUser)
                    call.respond(HttpStatusCode.OK, mapOf("Token" to jwtToken))
                }
            } else {
                call.respond(HttpStatusCode.Forbidden, ResponseStatus("Invalid email"))
            }
        }
    }
}