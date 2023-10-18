package com.carhop.dao.cars


import com.carhop.dto.cars.RegisterCarDTO
import com.carhop.dto.cars.UpdateCarDTO
import com.carhop.models.Car
import com.carhop.models.User


interface CarsDAOFacade {

    suspend fun registerCar(newCar: RegisterCarDTO): Car?
    suspend fun updateCar(updatedCar: UpdateCarDTO): Car?
    suspend fun getAllCars(): List<Car>
    suspend fun getCar(carId: Int): Car?

    suspend fun deleteCar(carId: Int)
    suspend fun getOwnerIdByCarId(requestedId: Int): Int?



}