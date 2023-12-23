package com.carhop.dao.users

import com.carhop.dto.users.LoginRequestDTO
import com.carhop.dto.users.RegisterUserDto
import com.carhop.dto.users.UpdateUserDTO
import com.carhop.models.User

interface UsersDAOFacade  {
    suspend fun registerUser(newUser: RegisterUserDto): User?
    suspend fun loginUser(loginRequest: LoginRequestDTO): User?
    suspend fun getUser(userId: Int): User?
    suspend fun getAllUsers(): List<User?>
    suspend fun updateUser(updatedUser: UpdateUserDTO, userId: Int): User?
    suspend fun deleteUser(userId: Int)

}