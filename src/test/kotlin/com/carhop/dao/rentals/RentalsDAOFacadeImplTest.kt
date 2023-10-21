package com.carhop.dao.rentals

import com.carhop.models.Rental
import io.ktor.server.testing.*
import kotlinx.datetime.*
import org.junit.Before
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse


class RentalsDAOFacadeImplTest {

    private lateinit var rentalsDAO: RentalsDAOFacade
    @Before
    fun setUp() {
        rentalsDAO = RentalsDAOFacadeImpl()
    }
    @Test
    suspend fun checkIfRentalIsInPast1() = testApplication {

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

        assertFalse(result)


    }
}