ALTER TABLE Cars DROP CONSTRAINT FKCars287283;
ALTER TABLE Rentals DROP CONSTRAINT FKRentals751592;
ALTER TABLE Rentals DROP CONSTRAINT FKRentals315856;
ALTER TABLE Rentals DROP CONSTRAINT FKRentals30431;
ALTER TABLE Rides DROP CONSTRAINT FKRides373182;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Cars CASCADE;
DROP TABLE IF EXISTS Rentals CASCADE;
DROP TABLE IF EXISTS Rides CASCADE;
CREATE TABLE Users (id int4 NOT NULL, first_name varchar(255) NOT NULL, last_name varchar(255) NOT NULL, email varchar(255) NOT NULL UNIQUE, password varchar(255) NOT NULL, driving_score int4 DEFAULT 0 NOT NULL, user_type varchar(255) DEFAULT USER NOT NULL, PRIMARY KEY (id));
CREATE TABLE Cars (id int4 NOT NULL, owner int4 NOT NULL, rental_price int4 NOT NULL, available int2 NOT NULL, brand_name varchar(255) NOT NULL, model_name varchar(255) NOT NULL, build_year int4 NOT NULL, num_of_seats int4 NOT NULL, purchase_price float8 NOT NULL, monthly_insurance_cost float8 NOT NULL, yearly_maintenance_cost float8 NOT NULL, range int4, fuel_type varchar(255), transmission varchar(255), PRIMARY KEY (id));
CREATE TABLE Rentals (id int4 NOT NULL, car_id int4 NOT NULL, owner_id int4 NOT NULL, renter_id int4 NOT NULL, start_date date NOT NULL, end_date date NOT NULL, cost float8 NOT NULL, PRIMARY KEY (id));
CREATE TABLE Rides (rental_id int4 NOT NULL, start_time int4, end_time int4, accumulated_score int4);
ALTER TABLE Cars ADD CONSTRAINT FKCars287283 FOREIGN KEY (owner) REFERENCES Users (id) ON UPDATE Cascade ON DELETE Cascade;
ALTER TABLE Rentals ADD CONSTRAINT FKRentals751592 FOREIGN KEY (owner_id) REFERENCES Users (id) ON UPDATE Cascade ON DELETE Set null;
ALTER TABLE Rentals ADD CONSTRAINT FKRentals315856 FOREIGN KEY (renter_id) REFERENCES Users (id);
ALTER TABLE Rentals ADD CONSTRAINT FKRentals30431 FOREIGN KEY (car_id) REFERENCES Cars (id);
ALTER TABLE Rides ADD CONSTRAINT FKRides373182 FOREIGN KEY (rental_id) REFERENCES Rentals (id);
