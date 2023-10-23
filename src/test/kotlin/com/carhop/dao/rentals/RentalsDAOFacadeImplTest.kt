package com.carhop.dao.rentals

import com.carhop.models.Rental
import io.ktor.server.testing.*
import kotlinx.datetime.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class RentalsDAOFacadeImplTest {

    @Test

    fun checkIfRentalIsInPast1() = testApplication {
            lateinit var rentalsDAO: RentalsDAOFacade
            rentalsDAO = RentalsDAOFacadeImpl()

            val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val futureStartDate = currentDate.plus(1, DateTimeUnit.DAY)
            val futureEndDate = currentDate.plus(2, DateTimeUnit.DAY)
            val rental = Rental(
                id = 1,
                carId = 1,
                renterId = 1,
                startDate = futureStartDate,
                endDate = futureEndDate,
                cost = 100.0
            )

            val result = rentalsDAO.checkIfRentalIsInPast(rental)
        assertTrue(result)


    }
    @Test

    fun checkIfRentalIsInPast2() = testApplication {
        lateinit var rentalsDAO: RentalsDAOFacade
        rentalsDAO = RentalsDAOFacadeImpl()

        val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val futureStartDate = currentDate.minus(2, DateTimeUnit.DAY)
        val futureEndDate = currentDate.minus(1, DateTimeUnit.DAY)
        val rental = Rental(
            id = 1,
            carId = 1,
            renterId = 1,
            startDate = futureStartDate,
            endDate = futureEndDate,
            cost = 100.0
        )

        val result = rentalsDAO.checkIfRentalIsInPast(rental)
        assertFalse(result)


    }
    @Test

    fun checkIfRentalIsInPast3() = testApplication {
        lateinit var rentalsDAO: RentalsDAOFacade
        rentalsDAO = RentalsDAOFacadeImpl()

        val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val futureStartDate = currentDate
        val futureEndDate = currentDate
        val rental = Rental(
            id = 1,
            carId = 1,
            renterId = 1,
            startDate = futureStartDate,
            endDate = futureEndDate,
            cost = 100.0
        )

        val result = rentalsDAO.checkIfRentalIsInPast(rental)
        System.out.println(result)
        assertFalse(result)


    }
}