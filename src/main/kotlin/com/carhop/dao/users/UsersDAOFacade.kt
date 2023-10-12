package com.carhop.dao.users

import com.carhop.dto.LoginRequestDTO
import com.carhop.dto.RegisterUserDto
import com.carhop.dto.UpdateUserDTO
import com.carhop.models.User
import org.jetbrains.exposed.sql.ResultRow

interface UsersDAOFacade  {
    suspend fun resultRowToUser(row: ResultRow): User
    suspend fun registerUser(newUser: RegisterUserDto): User?
    suspend fun loginUser(loginRequest: LoginRequestDTO): User?
    suspend fun getUser(userId: Int): User?
    suspend fun updateUser(updatedUser: UpdateUserDTO): User?
    suspend fun deleteUser(userId: Int)
}