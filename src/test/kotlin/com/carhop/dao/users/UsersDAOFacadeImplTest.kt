package com.carhop.dao.users


import com.carhop.dto.users.LoginRequestDTO
import com.carhop.dto.users.RegisterUserDto
import com.carhop.models.User
import com.carhop.plugins.DatabaseFactory
import com.carhop.plugins.configureRouting
import com.carhop.plugins.configureSecurity
import com.carhop.utils.TokenManager
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class UsersDAOFacadeImplTest {

    @Test
    fun registerUser() = testApplication {
        // Initialize the test environment with application configuration and JSON support
        application {
            configureSecurity()
            configureRouting()
        }
        //Create an HTTP client for the test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialize the database
        DatabaseFactory.init()
        // data for new user registration
        val userRegisterReqeust = RegisterUserDto("testUser", "testLastName", "test@email.com", "test", 0,"user")
        val json = Json.encodeToString(userRegisterReqeust)
        // Do a POST request to register the user
        val response = client.post("/users/register") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }
        val statusCode = response.status
        val responseBody = response.body<String>()
        //Check if the registration was successful
        assertEquals(HttpStatusCode.OK, statusCode)
        // Retrieve the registered user from the database
        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val registeredUser = LoginRequestDTO(userRegisterReqeust.email,userRegisterReqeust.password )
        val loginRegisteredUser = userDAO.loginUser(registeredUser)
        //Verify that the registered user matches the expected email
        if (loginRegisteredUser != null) {
            assertEquals(userRegisterReqeust.email, loginRegisteredUser.email)
        }
        // Delete the registered user
        val deleteRegisteredUser = loginRegisteredUser?.let { userDAO.deleteUser(it.id) }

    }

    @Test
    fun loginUser() = testApplication {
        // Initialize the test environment with application configuration and JSON support
        application {
            configureSecurity()
            configureRouting()
        }
        // Create an HTTP client for the test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialize the database
        DatabaseFactory.init()
        // user login information
        val loginRequest = LoginRequestDTO("Jeroen@test.com", "123456")
        val json = Json.encodeToString(loginRequest)
        //Do a POST request to log in
        val response = client.post("/users/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }
        // Check whether the login attempt was successful
        val statusCode = response.status
        val responseBody = response.body<String>()

        assertEquals(HttpStatusCode.OK, statusCode)

    }

    @Test
    fun getUser() = testApplication {
        // Initialize the test environment with application configuration and JSON support
        application {
            configureSecurity()
            configureRouting()
        }
        //Create an HTTP client for the test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialize the database
        DatabaseFactory.init()

        // Retrieve the expected user from the database and generate a JWT token for authentication
        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val Expecteduser = userDAO.getUser(2)
        val tokenManager = TokenManager()
            val token = Expecteduser?.let { tokenManager.generateJWTToken(it) }

        //Do a GET request to retrieve the user profile
        val response = client.get("/users/profile/2"){
            headers { append(HttpHeaders.Authorization, "Bearer " + token) }
        }.body<String>()

        val user = Json.decodeFromString<User>(response)

        // Verify that the retrieved user matches the expected user
        assertEquals(Expecteduser, user)


    }


}


