package com.carhop.dao.users

import com.carhop.dto.RegisterCarDTO
import com.carhop.dto.UpdateCarDTO
import com.carhop.entities.Cars
import com.carhop.entities.Users
import com.carhop.models.Car
import com.carhop.plugins.DatabaseFactory
import com.carhop.plugins.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class CarsDAOFacadeImpl : CarsDAOFacade {

    private fun resultRowToCar(row: ResultRow) = Car (
        id = row[Cars.id],
        ownerId = row[Cars.ownerId],
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
        val userConsists = !Users.select(Users.id eq newCar.UserId ).empty()
        if (isCaridUnique && userConsists ) {
            val insertStatement = Cars.insert {
                it[ownerId] = newCar.UserId
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

    override suspend fun updateCar (updatedCar: UpdateCarDTO): Car?  = dbQuery {
        val carToUpdate = Cars.select(Cars.id eq updatedCar.carId).map {resultRowToCar(it)}.singleOrNull()

        if (carToUpdate != null){

            val CarIdToUpdate = updatedCar.carId

            val CarUpdateStatement = Cars.update({ Cars.id eq updatedCar.carId }) {
                it[Cars.licensePlate] = updatedCar.licensePlate
                it[Cars.rentalPrice] = updatedCar.rentalPrice
                it[Cars.available] = updatedCar.available
                it[Cars.brandName] = updatedCar.brandName
                it[Cars.modelName] = updatedCar.modelName
                it[Cars.buildYear] = updatedCar.buildYear
                it[Cars.numOfSeats] = updatedCar.numOfSeats
                it[Cars.emissionCategory] = updatedCar.emissionCategory
                it[Cars.purchasePrice] = updatedCar.purchasePrice
                it[Cars.monthlyInsuranceCost] = updatedCar.monthlyInsuranceCost
                it[Cars.yearlyMaintenanceCost] = updatedCar.yearlyMaintenanceCost
            }
            if (CarUpdateStatement < 1){
                null
            }else{
                Cars.select { Cars.id eq CarIdToUpdate }.map { resultRowToCar(it) }.singleOrNull()
            }

        }else {
            null
        }

    }





}


val carDAO: CarsDAOFacade = CarsDAOFacadeImpl().apply {
    runBlocking {

    }
}
