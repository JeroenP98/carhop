package com.carhop.dao.users


import com.carhop.dto.RegisterCarDTO
import com.carhop.dto.UpdateCarDTO
import com.carhop.models.Car


interface CarsDAOFacade {

    suspend fun registerCar(newCar: RegisterCarDTO): Car?
    suspend fun updateCar(updatedCar: UpdateCarDTO): Car?
    suspend fun searchCars(): List<Car>
    suspend fun getCar(carId: Int): Car?

    suspend fun deleteCar(carId: Int)
    suspend fun getOwnerIdByCarId(requestedId: Int): Int?

}