package com.carhop.dao.users


import com.carhop.dto.users.RegisterUserDto
import com.carhop.models.User
import com.carhop.module
import com.carhop.plugins.DatabaseFactory
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail


class UsersDAOFacadeImplTest {

    @Test
    fun registerUser() = runBlocking {

        val server = embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)

        try {
            System.out.println("stap1")
            server.start(wait = false)
            System.out.println("stap2")
            DatabaseFactory.init()
            System.out.println("stap3")
            val newUser = RegisterUserDto(
                "Jero",
                "de Leeuw",
                "Jero@test.com",
                "123456",
                0,
                "user"
            )
            System.out.println(newUser)
            val registeredUser = userDAO.registerUser(newUser)
            System.out.println(registeredUser)
            val expectedUser = registeredUser?.let {
                User(
                    id = it.id,
                    firstName = newUser.firstName, // Fix here
                    lastName = newUser.lastName,
                    email = newUser.email,
                    password = newUser.password,
                    drivingScore = newUser.drivingScore,
                    userType = newUser.userType
                )
            }
            System.out.println(expectedUser)

            assertEquals(expectedUser, registeredUser)
        } catch (e: Exception) {
            fail("Exception occurred during test: ${e.message}")
        } finally {
            server.stop(0L, 0L)
        }
    }

    @Test
    fun loginUser() = testApplication {

        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                isLenient = true})
            }
        }
        System.out.println("client:")
        System.out.println(client)
        val response = client.post("users/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterUserDto("Jan", "de Leeuw", "Jeroen@tst.com", "123456", 0,"user"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getUser() {
    }

    @Test
    fun updateUser() {
    }

    @Test
    fun deleteUser() {
    }
}