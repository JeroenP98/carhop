package com.carhop.dao.users

import com.carhop.dto.RegisterCarDTO
import com.carhop.entities.Cars
import com.carhop.models.Car
import com.carhop.plugins.DatabaseFactory
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class CarsDAOFacadeImpl : CarsDAOFacade {

    private fun resultRowToCar(row: ResultRow) = Car (
        id = row[Cars.id],
        ownerId = row[Cars.owner],
        licensePlate = row[Cars.licensePlate],
        rentalPrice = row[Cars.rentalPrice],
        available = row[Cars.available],
        brandName = row[Cars.brandName],
        modelName = row[Cars.modelName],
        buildYear = row[Cars.buildYear],
        numOfSeats = row[Cars.numOfSeats],
        emissionCategory = row[Cars.emissionCategory],
        purchasePrice = row[Cars.purchasePrice],
        monthlyInsuranceCost = row[Cars.monthlyInsuranceCost],
        yearlyMaintenanceCost = row[Cars.yearlyMaintenanceCost]
    )

    override suspend fun registerCar(newCar: RegisterCarDTO): Car? = DatabaseFactory.dbQuery {
        val isCaridUnique = Cars.select(Cars.licensePlate eq newCar.licensePlate).empty()
        if (isCaridUnique) {
            val insertStatement = Cars.insert {
                it[owner] = newCar.numOfSeats
                it[licensePlate] = newCar.licensePlate
                it[rentalPrice] = newCar.rentalPrice
                it[available] = newCar.available
                it[brandName] = newCar.brandName
                it[modelName] = newCar.modelName
                it[buildYear] = newCar.buildYear
                it[numOfSeats] = newCar.numOfSeats
                it[emissionCategory] = newCar.emissionCategory
                it[purchasePrice] = newCar.purchasePrice
                it[monthlyInsuranceCost] = newCar.monthlyInsuranceCost
                it[yearlyMaintenanceCost] = newCar.yearlyMaintenanceCost
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCar)
        } else {
            null
        }
    }
}

val carDAO: CarsDAOFacade = CarsDAOFacadeImpl().apply {
    runBlocking {

    }
}
