package com.carhop.entities


import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Cars: Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val owner: Column<Int> = integer("owner")
    val licensePlate: Column<String> = varchar("license_plate", 255)
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

    override val primaryKey = PrimaryKey(id)
}
