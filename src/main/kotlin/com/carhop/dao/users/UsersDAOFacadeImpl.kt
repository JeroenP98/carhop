package com.carhop.dao.users

import com.carhop.dto.LoginRequestDTO
import com.carhop.dto.RegisterUserDto
import com.carhop.dto.UpdateUserDTO
import com.carhop.entities.Users
import com.carhop.models.User
import com.carhop.plugins.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UsersDAOFacadeImpl : UsersDAOFacade {
    private fun resultRowToUser(row: ResultRow) = User (
        id = row[Users.id],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        email = row[Users.email],
        password = row[Users.password],
        drivingScore = row[Users.drivingScore],
        userType = row[Users.userType]
    )

    override suspend fun registerUser(newUser: RegisterUserDto): User? = dbQuery {
        val isEmailUnique = Users.select(Users.email eq newUser.email).empty()
        if (isEmailUnique) {
            val insertStatement = Users.insert {
                it[Users.firstName] = newUser.firstName
                it[Users.lastName] = newUser.lastName
                it[Users.email] = newUser.email
                it[Users.password] = newUser.password
                it[Users.userType] = newUser.userType
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        } else  {
            null
        }
    }

    override suspend fun loginUser(loginRequest: LoginRequestDTO): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: Int): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(updatedUser: UpdateUserDTO): User? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: Int) {
        TODO("Not yet implemented")
    }
}

val userDAO: UsersDAOFacade = UsersDAOFacadeImpl().apply {
    runBlocking {

    }
}