package com.carhop.dao.rentals

import com.carhop.dao.users.UsersDAOFacadeImpl.Companion.resultRowToUser
import com.carhop.dao.cars.CarsDAOFacadeImpl.Companion.resultRowToCar
import com.carhop.dao.cars.carDAO
import com.carhop.dao.users.userDAO
import com.carhop.dto.rentals.RegisterRentalDTO
import com.carhop.entities.Cars
import com.carhop.entities.Rentals
import com.carhop.entities.Users
import com.carhop.models.Rental
import com.carhop.models.User
import com.carhop.plugins.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class RentalsDAOFacadeImpl : RentalsDAOFacade {

    private fun resultRowToRental(row: ResultRow) = Rental (
        id = row[Rentals.id],
        carId = row[Rentals.carId],
        renterId = row[Rentals.renterId],
        startDate = row[Rentals.startDate].toKotlinLocalDate(),
        endDate = row[Rentals.endDate].toKotlinLocalDate(),
        cost = row[Rentals.cost],
    )
    override suspend fun registerRental(registerRentalDTO: RegisterRentalDTO): Rental? = dbQuery {
        val insertStatement = transaction {
            Rentals.insert {
                it[carId] = registerRentalDTO.carId
                it[renterId] = registerRentalDTO.renterId
                it[startDate] = registerRentalDTO.startDate.toJavaLocalDate()
                it[endDate] = registerRentalDTO.endDate.toJavaLocalDate()
                it[cost] = registerRentalDTO.cost
            }
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToRental)
    }

    override suspend fun getRental(rentalId: Int): Rental? {
        val rental = transaction {
            Rentals.select { Rentals.id eq  rentalId}.map { resultRowToRental(it) }.singleOrNull()

        }

        return rental
    }

    override suspend fun getAllRentals(): List<Rental> = dbQuery {
        Rentals.selectAll().map(::resultRowToRental)
    }

    override suspend fun checkTimeAvailability(carId: Int, startDate: LocalDate, endDate: LocalDate): Boolean {
        // determine if there are any overlapping reservations for a given car by asserting (StartA <= EndB) and (EndA >= StartB)
        val overlappingReservation = transaction {
            Rentals.select {
                (Rentals.carId eq carId) and
                        (Rentals.startDate lessEq endDate.toJavaLocalDate()) and
                        (Rentals.endDate greaterEq startDate.toJavaLocalDate())
            }.firstOrNull()
        }
        //return true if the query produced no results, indicating that there is no overlap
        return overlappingReservation == null

    }

    override suspend fun checkCarAvailability(carId: Int): Boolean {
        return transaction {
            val carRow = Cars.select { (Cars.id eq carId) and (Cars.available eq true) }.singleOrNull()
            carRow != null
        }
    }

    override suspend fun deleteRental(rentalId: Int) {

        transaction {
            Rentals.deleteWhere { id eq rentalId }
        }

    }

    override suspend fun returnOwner(rentalId: Int): User? {

        val rental = this.getRental(rentalId)
        return if (rental != null) {
            val carId = rental.carId
            val car = carDAO.getCar(carId)
            if (car != null) {
                val ownerId = car.ownerId
                val owner = userDAO.getUser(ownerId)
                owner
            } else {
                null
            }

        } else {
            null
        }
    }

    override suspend fun returnRenter(rentalId: Int): User? {
        val rental = this.getRental(rentalId)

        return if (rental != null) {
            val renterId = rental.renterId
            val renter = userDAO.getUser(renterId)
            renter
        } else {
            null
        }
    }

    override suspend fun checkIfRentalIsInPast(rental: Rental): Boolean {
        val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return rental.startDate > currentDate && rental.endDate > currentDate
    }
}

val rentalDao: RentalsDAOFacade = RentalsDAOFacadeImpl().apply {
    runBlocking {

    }
}

