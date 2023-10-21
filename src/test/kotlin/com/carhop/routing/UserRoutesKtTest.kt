package com.carhop.routing

import com.carhop.dao.users.userDAO
import com.carhop.dto.users.LoginRequestDTO
import com.carhop.models.User
import com.carhop.plugins.DatabaseFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UserRoutesKtTest {

    @Test
    fun testUserLoginRoute() = testApplication {
        val response = client.post("users/login") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(LoginRequestDTO("jeroen@admin.com", "12345"))
        }
        assertEquals(response.status, HttpStatusCode.OK)
    }

}
