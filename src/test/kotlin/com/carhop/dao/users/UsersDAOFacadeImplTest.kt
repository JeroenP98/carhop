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
        // Creëer een HTTP-client voor de test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialiseer de database
        DatabaseFactory.init()
        // gegevens voor nieuwe gebruikerregistratie
        val userRegisterReqeust = RegisterUserDto("testUser", "testLastName", "test@email.com", "test", 0,"user")
        val json = Json.encodeToString(userRegisterReqeust)
        // Voer een POST-verzoek uit om de gebruiker te registreren
        val response = client.post("/users/register") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }
        val statusCode = response.status
        val responseBody = response.body<String>()
        // Controleer of de registratie succesvol was
        assertEquals(HttpStatusCode.OK, statusCode)
        // Haal de geregistreerde gebruiker op uit de database
        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val registeredUser = LoginRequestDTO(userRegisterReqeust.email,userRegisterReqeust.password )
        val loginRegisteredUser = userDAO.loginUser(registeredUser)
        // Controleer of de geregistreerde gebruiker overeenkomt met de verwachte e-mail
        if (loginRegisteredUser != null) {
            assertEquals(userRegisterReqeust.email, loginRegisteredUser.email)
        }
        // Verwijder de geregistreerde gebruiker
        val deleteRegisteredUser = loginRegisteredUser?.let { userDAO.deleteUser(it.id) }

    }

    @Test
    fun loginUser() = testApplication {
        // Initialiseer de testomgeving met applicatieconfiguratie en JSON-ondersteuning
        application {
            configureSecurity()
            configureRouting()
        }
        // Creëer een HTTP-client voor de test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialiseer de database
        DatabaseFactory.init()
        // gegevens voor gebruikersaanmelding
        val loginRequest = LoginRequestDTO("jeroen@user.com", "12345")
        val json = Json.encodeToString(loginRequest)
        // Voer een POST-verzoek uit om in te logge
        val response = client.post("/users/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json)
        }
        // Controleer of de inlogpoging succesvol was
        val statusCode = response.status
        val responseBody = response.body<String>()

        assertEquals(HttpStatusCode.OK, statusCode)

    }

    @Test
    fun getUser() = testApplication {
        // Initialiseer de testomgeving met applicatieconfiguratie en JSON-ondersteuning
        application {
            configureSecurity()
            configureRouting()
        }
        // Creëer een HTTP-client voor de test
        val client = createClient {
            this@testApplication.install(ContentNegotiation) {
                json(Json { prettyPrint = true
                    isLenient = true})
            }
        }
        // Initialiseer de database
        DatabaseFactory.init()

        // Haal de verwachte gebruiker op uit de database en genereer een JWT-token voor authenticatie
        lateinit var userDAO: UsersDAOFacade
        userDAO = UsersDAOFacadeImpl()
        val Expecteduser = userDAO.getUser(2)
        val tokenManager = TokenManager()
            val token = Expecteduser?.let { tokenManager.generateJWTToken(it) }

        // Voer een GET-verzoek uit om het gebruikersprofiel op te halen
        val response = client.get("/users/profile/2"){
            headers { append(HttpHeaders.Authorization, "Bearer " + token) }
        }.body<String>()

        val user = Json.decodeFromString<User>(response)

        // Controleer of de opgehaalde gebruiker overeenkomt met de verwachte gebruiker
        assertEquals(Expecteduser, user)


    }


}


