package com.carhop.entities

import kotlinx.datetime.toJavaLocalDate
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Rentals: Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val carId: Column<Int> = integer("car_id").references(Cars.id)
    val renterId: Column<Int> = integer("renter_id").references(Users.id)

    //Exposed only allows java datetime objects. value of the date is transformed using .toJavaLocalDate()
    val startDate: Column<java.time.LocalDate> = date("start_date").clientDefault { kotlinx.datetime.LocalDate(2000, 1, 1).toJavaLocalDate() }
    val endDate: Column<java.time.LocalDate> = date("end_date").clientDefault { kotlinx.datetime.LocalDate(2000, 1, 1).toJavaLocalDate() }
    val cost: Column<Double> = double("cost")

    override val primaryKey = PrimaryKey(id)
}