package com.carhop.entities

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

//Exposed table used for storing user records
object Users: Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val firstName: Column<String> = varchar("first_name",255)
    val lastName: Column<String> = varchar("last_name",255)
    val email: Column<String> = varchar("email",255).uniqueIndex()
    val password: Column<String> = varchar("password",255)
    val drivingScore: Column<Int> = integer("driving_score").default(0)
    val userType: Column<String> = varchar("user_type", 255).default("user")

    override val primaryKey = PrimaryKey(id)
}

fun checkUserExists(userId: Int): Boolean {
    return transaction {
        val existingUser = Users.select { Users.id eq userId }.singleOrNull()
        existingUser != null
    }
}