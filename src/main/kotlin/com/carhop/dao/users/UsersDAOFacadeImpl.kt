package com.carhop.dao.users

import com.carhop.dto.LoginRequestDTO
import com.carhop.dto.RegisterUserDto
import com.carhop.dto.UpdateUserDTO
import com.carhop.entities.Users
import com.carhop.models.User
import org.jetbrains.exposed.sql.ResultRow

class UsersDAOFacadeImpl : UsersDAOFacade {
    override suspend fun resultRowToUser(row: ResultRow) = User (
        id = row[Users.id],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        email = row[Users.email],
        password = row[Users.password],
        drivingScore = row[Users.drivingScore],
        userType = row[Users.userType]
    )

    override suspend fun registerUser(newUser: RegisterUserDto): User? {
        TODO("Not yet implemented")
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