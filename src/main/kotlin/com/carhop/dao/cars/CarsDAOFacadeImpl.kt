package com.carhop.dao.cars

import com.carhop.dto.cars.RegisterCarDTO
import com.carhop.dto.cars.UpdateCarDTO
import com.carhop.entities.Cars
import com.carhop.entities.Users
import com.carhop.models.Car
import com.carhop.plugins.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CarsDAOFacadeImpl : CarsDAOFacade {

    companion object {
        fun resultRowToCar(row: ResultRow) = Car (
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
            yearlyMaintenanceCost = row[Cars.yearlyMaintenanceCost],
            range = row[Cars.range],
            fuelType = row[Cars.fuelType],
            transmission = row[Cars.transmission],

            )
    }




    override suspend fun registerCar(newCar: RegisterCarDTO): Car? = dbQuery {
        val isCarIdUnique = Cars.select(Cars.licensePlate eq newCar.licensePlate).empty()
        val userConsists = !Users.select(Users.id eq newCar.UserId ).empty()
        if (isCarIdUnique && userConsists ) {
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
                it[range] = newCar.range
                it[fuelType] = newCar.fuelType
                it[transmission] = newCar.transmission
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCar)
        } else {
            null
        }
    }

    override suspend fun updateCar (updatedCar: UpdateCarDTO): Car?  = dbQuery {
        val carToUpdate = Cars.select(Cars.licensePlate eq updatedCar.licensePlate).map {resultRowToCar(it)}.singleOrNull()

        if (carToUpdate != null){

            val carIdToUpdate = updatedCar.licensePlate

            val carUpdateStatement = Cars.update({ Cars.licensePlate eq updatedCar.licensePlate }) {
                it[licensePlate] = updatedCar.licensePlate
                it[rentalPrice] = updatedCar.rentalPrice
                it[available] = updatedCar.available
                it[brandName] = updatedCar.brandName
                it[modelName] = updatedCar.modelName
                it[buildYear] = updatedCar.buildYear
                it[numOfSeats] = updatedCar.numOfSeats
                it[emissionCategory] = updatedCar.emissionCategory
                it[purchasePrice] = updatedCar.purchasePrice
                it[monthlyInsuranceCost] = updatedCar.monthlyInsuranceCost
                it[yearlyMaintenanceCost] = updatedCar.yearlyMaintenanceCost
                it[range] = updatedCar.range
                it[fuelType] = updatedCar.fuelType
                it[transmission] = updatedCar.transmission
            }
            if (carUpdateStatement < 1){
                null
            }else{
                Cars.select { Cars.licensePlate eq carIdToUpdate }.map { resultRowToCar(it) }.singleOrNull()
            }

        }else {
            null
        }

    }

    override suspend fun getAllCars(): List<Car> = dbQuery {
        Cars.selectAll().map(::resultRowToCar)
    }



    override suspend fun getCar(carId: Int): Car? {
        val car = transaction {
            Cars.select { Cars.id eq carId }.map { resultRowToCar(it) }.singleOrNull()
        }

        return car
    }

    override suspend fun deleteCar(carId: Int) {
        transaction {
            Cars.deleteWhere { id eq carId }
        }
    }

    override suspend fun getOwnerIdByCarId(requestedId: Int): Int? {
        var ownerId: Int? = null

        transaction {
            ownerId = Cars.select { Cars.id eq requestedId }
                .singleOrNull()
                ?.get(Cars.ownerId)
        }

        return ownerId
    }


}


val carDAO: CarsDAOFacade = CarsDAOFacadeImpl().apply {
    runBlocking {

    }
}
