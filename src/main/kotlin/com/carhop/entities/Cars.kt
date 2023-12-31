package com.carhop.entities


import com.carhop.models.FuelType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

//Exposed table used for storing car records
object Cars: Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val ownerId: Column<Int> = integer("owner_id").references(Users.id)
    val licensePlate: Column<String> = varchar("license_plate", 255).uniqueIndex()
    val rentalPrice: Column<Double> = double("rental_price")
    val available: Column<Boolean> = bool("available")
    val brandName: Column<String> = varchar("brand_name", 255)
    val modelName: Column<String> = varchar("model_name", 255)
    val buildYear: Column<Int> = integer("build_year")
    val numOfSeats: Column<Int> = integer("num_of_seats")
    val emissionCategory: Column<Char> = char("emission_category")
    val purchasePrice: Column<Double> = double("purchase_price")
    val monthlyInsuranceCost: Column<Double> = double("monthly_insurance_cost")
    val yearlyMaintenanceCost: Column<Double> = double("yearly_maintenance_cost")
    val range: Column<Double?> = double("range").nullable()
    val fuelType: Column<FuelType> = enumerationByName("fuel_type", 50, FuelType::class)
    val transmission: Column<String?> = varchar("transmission",255).nullable()
    val latitude: Column<Double> = double("latitude")
    val longitude: Column<Double> = double("longitude")


    override val primaryKey = PrimaryKey(id)
}


fun checkCarExists(carId: Int): Boolean {
    return transaction {
        val existingCar = Cars.select { Cars.id eq carId }.singleOrNull()
        existingCar != null
    }
}