package com.carhop.plugins

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

//object for sotring database connection variables
object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://localhost:5432/carhop"
        val user = "postgres"
        val password = "postgres"
        val database = Database.connect(url = jdbcURL, driver = driverClassName, user, password)

    }

    // create variable to be used for database transactions in coroutines
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}