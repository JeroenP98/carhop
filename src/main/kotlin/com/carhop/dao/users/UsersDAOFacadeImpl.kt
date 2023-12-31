package com.carhop.dao.users

import com.carhop.dto.users.LoginRequestDTO
import com.carhop.dto.users.RegisterUserDto
import com.carhop.dto.users.UpdateUserDTO
import com.carhop.entities.Users
import com.carhop.models.User
import com.carhop.plugins.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UsersDAOFacadeImpl : UsersDAOFacade {
    companion object {
        fun resultRowToUser(row: ResultRow) = User (
            // function for mapping the record returned by sql expression to user class
            id = row[Users.id],
            firstName = row[Users.firstName],
            lastName = row[Users.lastName],
            email = row[Users.email],
            password = row[Users.password],
            drivingScore = row[Users.drivingScore],
            userType = row[Users.userType]
        )
    }

    //add new user record to database
    override suspend fun registerUser(newUser: RegisterUserDto): User? = dbQuery {
        // verify if user email is unique
        val isEmailUnique = Users.select(Users.email eq newUser.email).empty()

        if (isEmailUnique) {
            val insertStatement = Users.insert {
                it[firstName] = newUser.firstName
                it[lastName] = newUser.lastName
                it[email] = newUser.email
                it[password] = newUser.password
                it[userType] = newUser.userType
            }

            //return resulted record created as user object
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        } else  {
            null
        }
    }

    //check user credentials and return User object if credentials are correct
    override suspend fun loginUser(loginRequest: LoginRequestDTO): User? {
        val user = transaction {
            Users.select { Users.email eq loginRequest.email }.map { resultRowToUser(it) }.singleOrNull()
        }

        return if (user != null && user.password == loginRequest.password) {
            user
        } else {
            null
        }
    }

    //get single user based on id
    override suspend fun getUser(userId: Int): User? {
        //return user by id
        val user = transaction {
            Users.select { Users.id eq userId }.map { resultRowToUser(it) }.singleOrNull()
        }

        return user
    }

    //get all users
    override suspend fun getAllUsers(): List<User?> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    //update a user
    override suspend fun updateUser(updatedUser: UpdateUserDTO, userId: Int): User?  = dbQuery{

        //retrieve the user to be updated
        val userToUpdate = Users.select { Users.id eq userId }.map { resultRowToUser(it) }.singleOrNull()


        if (userToUpdate != null) {
            //store the user id, as it is the only unique value unchangeable
            val idToUpdate = userToUpdate.id

            //execute update statement
            val updateStatement = Users.update({Users.id eq userId}) {
                it[firstName] = updatedUser.firstName
                it[lastName] = updatedUser.lastName
                it[email] = updatedUser.email
                it[password] = updatedUser.password

            }

            if (updateStatement < 1){
                //if no. rows updated is lower than one, return null
                null
            } else {
                // else, return the updated user retrieved by the id stored previously
                Users.select { Users.id eq idToUpdate }.map { resultRowToUser(it) }.singleOrNull()
            }
        } else {
            //if no user was found to be updated, return null
            null
        }
    }

    //delete user based on id
    override suspend fun deleteUser(userId: Int) {
        transaction {
            Users.deleteWhere { id eq userId }
        }
    }

}


// implement the user DAO class to be used in routing handling
val userDAO: UsersDAOFacade = UsersDAOFacadeImpl()