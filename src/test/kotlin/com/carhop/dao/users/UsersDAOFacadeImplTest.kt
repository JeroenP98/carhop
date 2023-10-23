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
        application {
            configureSecurity()
            configureRouting()
        }
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        DatabaseFactory.init()

        val userRegisterReqeust = RegisterUserDto("testUser", "testLastName", "test@email.com", "test", 0,"user")
        val json = Json.encodeToString(userRegisterReqeust)

        val response = client.post("/users/register") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }
        val statusCode = response.status
        val responseBody = response.body<String>()

        assertEquals(HttpStatusCode.OK, statusCode)

        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val registeredUser = LoginRequestDTO(userRegisterReqeust.email,userRegisterReqeust.password )
        val loginRegisteredUser = userDAO.loginUser(registeredUser)

        if (loginRegisteredUser != null) {
            assertEquals(userRegisterReqeust.email, loginRegisteredUser.email)
        }
        val deleteRegisteredUser = loginRegisteredUser?.let { userDAO.deleteUser(it.id) }

    }

    @Test
    fun loginUser() = testApplication {
        application {
            configureSecurity()
            configureRouting()
        }
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        DatabaseFactory.init()

        val loginRequest = LoginRequestDTO("Jeroen@test.com", "123456")
        val json = Json.encodeToString(loginRequest)

        val response = client.post("/users/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }

        val statusCode = response.status
        val responseBody = response.body<String>()

        assertEquals(HttpStatusCode.OK, statusCode)

        System.out.println("response:")
        System.out.println(responseBody)
    }

    @Test
    fun getUser() = testApplication {
        application {
            configureSecurity()
            configureRouting()
        }
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        DatabaseFactory.init()

        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val Expecteduser = userDAO.getUser(2)
        val tokenManager = TokenManager()
            val token = Expecteduser?.let { tokenManager.generateJWTToken(it) }

        val response = client.get("/users/profile/2"){
            headers { append(HttpHeaders.Authorization, "Bearer " + token) }

        }.body<String>()



        //val responseBody = response.body<String>()
        System.out.println("response body:")
        System.out.println(response)
        val user = Json.decodeFromString<User>(response)
        System.out.println("response user:")
        System.out.println(user)




        assertEquals(Expecteduser, Expecteduser)

    }


}


