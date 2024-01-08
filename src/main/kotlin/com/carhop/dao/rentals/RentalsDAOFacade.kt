package com.carhop.dao.rentals

import com.carhop.dto.rentals.RegisterRentalDTO
import com.carhop.models.Car
import com.carhop.models.Rental
import com.carhop.models.User
import kotlinx.datetime.LocalDate

interface RentalsDAOFacade {

    suspend fun registerRental(registerRentalDTO: RegisterRentalDTO): Rental?
    suspend fun getRental(rentalId: Int): Rental?
    suspend fun getAllRentals(): List<Rental>
    suspend fun checkTimeAvailability(carId: Int, startDate: LocalDate, endDate: LocalDate): Boolean
    suspend fun checkCarAvailability(carId: Int): Boolean
    suspend fun deleteRental(rentalId: Int)
    suspend fun returnOwner(rentalId: Int): User?
    suspend fun returnRenter(rentalId: Int): User?
    suspend fun checkIfRentalIsInPast(rental: Rental): Boolean

    suspend fun getAllUserRentals(userId: Int): List<Rental>?

    suspend fun getAllUserRentedOutCars(userId: Int): List<Rental>?
    suspend fun getAllRentalsByCarId(carId: Int): List<Rental>
}