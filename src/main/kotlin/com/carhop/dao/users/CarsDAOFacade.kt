package com.carhop.dao.users


import com.carhop.dto.RegisterCarDTO
import com.carhop.models.Car


interface CarsDAOFacade {

    suspend fun registerCar(newCar: RegisterCarDTO): Car?


}