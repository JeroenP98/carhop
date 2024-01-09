package com.carhop.dao.cars


import com.carhop.dto.cars.CarWithImageResponse
import com.carhop.dto.cars.RegisterCarDTO
import com.carhop.dto.cars.UpdateCarDTO
import com.carhop.models.Car
import com.carhop.models.CarLocation
import com.carhop.models.User


interface CarsDAOFacade {

    suspend fun registerCar(newCar: RegisterCarDTO): CarWithImageResponse?
    suspend fun updateCar(updatedCar: UpdateCarDTO): Car?
    suspend fun getAllCars(): List<CarWithImageResponse>
    suspend fun getCar(carId: Int): Car?

    suspend fun getCarWithImage(carId: Int): CarWithImageResponse?

    suspend fun getCarLocation(carId: Int): CarLocation?

    suspend fun deleteCar(carId: Int)
    suspend fun getOwnerIdByCarId(requestedId: Int): Int?
    suspend fun getTotalCostOfOwnership(carId: Int): Double?

    suspend fun getAllUserCars(userId: Int): List<CarWithImageResponse>?


}