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
import java.util.*

class CarsDAOFacadeImpl : CarsDAOFacade {

    companion object {
        // function for transforming SQL query results to Car object
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
        //check if license plate exist and owner exists
        val isLicensePlateUnique = Cars.select(Cars.licensePlate eq newCar.licensePlate).empty()
        val userConsists = !Users.select(Users.id eq newCar.UserId ).empty()

        if (isLicensePlateUnique && userConsists ) {
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
            //return the resulted value as a Car object
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCar)
        } else {
            null
        }
    }

    override suspend fun updateCar (updatedCar: UpdateCarDTO): Car?  = dbQuery {
        //retrieve car object to update and check if it exists
        val carToUpdate = Cars.select(Cars.licensePlate eq updatedCar.licensePlate).map {resultRowToCar(it)}.singleOrNull()
        if (carToUpdate != null){
            //use the license plate as unique identifier for the car
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
            /*if the no. updated rows is lower than one (meaning no rows were updated), return null.
            else, return the updated car as Car object*/
            if (carUpdateStatement < 1){
                null
            }else{

                Cars.select { Cars.licensePlate eq carIdToUpdate }.map { resultRowToCar(it) }.singleOrNull()
            }

        }else {
            null
        }

    }

    //return all cars
    override suspend fun getAllCars(): List<Car> = dbQuery {
        Cars.selectAll().map(::resultRowToCar)
    }


    // return single car based on given id value
    override suspend fun getCar(carId: Int): Car? {
        val car = transaction {
            Cars.select { Cars.id eq carId }.map { resultRowToCar(it) }.singleOrNull()
        }

        return car
    }

    //delete car based on id value
    override suspend fun deleteCar(carId: Int) {
        transaction {
            Cars.deleteWhere { id eq carId }
        }
    }

    //return the ownerId value based on a car id
    override suspend fun getOwnerIdByCarId(requestedId: Int): Int? {
        var ownerId: Int? = null

        transaction {
            ownerId = Cars.select { Cars.id eq requestedId }
                .singleOrNull()
                ?.get(Cars.ownerId)
        }

        return ownerId
    }

    override suspend fun getTotalCostOfOwnership(carId: Int): Double? {
        val car = this.getCar(carId)
        if (car != null) {
            val fuelType = car.fuelType.fuelType.lowercase()
            val monthlyInsurance = car.monthlyInsuranceCost
            val yearlyMaintenance = car.yearlyMaintenanceCost
            val purchasePrice = car.purchasePrice

            // Define TCO variables
            val depreciation: Double
            val fuelCost: Double
            val otherCosts: Double
            var totalCostOfOwnership: Double


            when (fuelType) {
                "electric" -> {
                    // Define TCO calculations for electric cars
                    depreciation = purchasePrice / 10.0 // 10% depreciation per year
                    fuelCost = 0.0 // Electric cars don't have fuel costs
                    otherCosts = yearlyMaintenance + (monthlyInsurance * 12.0) + depreciation
                    totalCostOfOwnership = purchasePrice + otherCosts
                }
                "hybrid" -> {
                    // Define TCO calculations for hybrid cars
                    depreciation = purchasePrice / 12.0 // 8% depreciation per year
                    fuelCost = 2000.0 // 2,000 fuel cost per year
                    otherCosts = yearlyMaintenance + (monthlyInsurance * 12.0) + depreciation
                    totalCostOfOwnership = purchasePrice + otherCosts + fuelCost
                }
                "gasoline" -> {
                    // Define TCO calculations for gasoline cars
                    depreciation = purchasePrice / 8.0 // Example: 12.5% depreciation per year
                    fuelCost = 3000.0 // 3,000 fuel cost per year
                    otherCosts = yearlyMaintenance + (monthlyInsurance * 12.0) + depreciation
                    totalCostOfOwnership = purchasePrice + otherCosts + fuelCost
                }
                else -> return null // Unknown fuel type
            }
            // round return value to two decimal places
            return String.format(Locale.US, "%.2f", totalCostOfOwnership).toDouble()
        }
        return null
    }


}

// initialize the car DAO
val carDAO: CarsDAOFacade = CarsDAOFacadeImpl()
