package com.carhop.dao.users

import com.carhop.dto.LoginRequestDTO
import com.carhop.dto.RegisterUserDto
import com.carhop.dto.UpdateUserDTO
import com.carhop.models.User

interface UsersDAOFacade  {
    suspend fun registerUser(newUser: RegisterUserDto): User?
    suspend fun loginUser(loginRequest: LoginRequestDTO): User?
    suspend fun getUser(userId: Int): User?
    suspend fun updateUser(updatedUser: UpdateUserDTO): User?
    suspend fun deleteUser(userId: Int)
}