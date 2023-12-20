DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Cars CASCADE;
DROP TABLE IF EXISTS Rentals CASCADE;
DROP TABLE IF EXISTS Rides CASCADE;

CREATE TABLE Users
(
    id            SERIAL                      NOT NULL,
    first_name    varchar(255)                NOT NULL,
    last_name     varchar(255)                NOT NULL,
    email         varchar(255)                NOT NULL UNIQUE,
    password      varchar(255)                NOT NULL,
    driving_score int4         DEFAULT 0      NOT NULL,
    user_type     varchar(255) DEFAULT 'user' NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE Cars
(
    id                      SERIAL       NOT NULL,
    owner_id                int4         NOT NULL,
    license_plate           varchar(255) NOT NULL UNIQUE,
    rental_price            int4         NOT NULL,
    available               boolean      NOT NULL,
    brand_name              varchar(255) NOT NULL,
    model_name              varchar(255) NOT NULL,
    build_year              int4         NOT NULL,
    num_of_seats            int4         NOT NULL,
    emission_category       char(1)      NOT NULL,
    purchase_price          float8       NOT NULL,
    monthly_insurance_cost  float8       NOT NULL,
    yearly_maintenance_cost float8       NOT NULL,
    range                   int4,
    fuel_type               varchar(255),
    transmission            varchar(255),
    latitude                float8       NOT NULL,
    longitude               float8       NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE Rentals
(
    id         SERIAL NOT NULL,
    car_id     int4   NOT NULL,
    renter_id  int4   NOT NULL,
    start_date date   NOT NULL,
    end_date   date   NOT NULL,
    cost       float8 NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE Rides
(
    rental_id         int4 NOT NULL,
    start_time        int4,
    end_time          int4,
    accumulated_score int4
);

ALTER TABLE Cars
    ADD CONSTRAINT FKCars287283 FOREIGN KEY (owner_id) REFERENCES Users (id) ON UPDATE Cascade ON DELETE Cascade;
ALTER TABLE Rentals
    ADD CONSTRAINT FKRentals315856 FOREIGN KEY (renter_id) REFERENCES Users (id);
ALTER TABLE Rentals
    ADD CONSTRAINT FKRentals30431 FOREIGN KEY (car_id) REFERENCES Cars (id);
ALTER TABLE Rides
    ADD CONSTRAINT FKRides373182 FOREIGN KEY (rental_id) REFERENCES Rentals (id);

CREATE
OR REPLACE FUNCTION prevent_owner_renter_match()
RETURNS TRIGGER AS $$
BEGIN
  IF
EXISTS (
    SELECT 1
    FROM Cars
    WHERE Cars.id = NEW.car_id
      AND Cars.owner_id = NEW.renter_id
  ) THEN
    RAISE EXCEPTION 'Owner cannot be the same as the renter';
END IF;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER prevent_owner_renter_match_trigger
    BEFORE INSERT
    ON Rentals
    FOR EACH ROW
    EXECUTE FUNCTION prevent_owner_renter_match();


insert into users (first_name, last_name, email, password, user_type)
values ('Jeroen', 'Post', 'jeroen@admin.com', '12345', 'admin');
insert into users (first_name, last_name, email, password, user_type)
values ('Jeroen', 'Post', 'jeroen@user.com', '12345', 'user');
insert into users (first_name, last_name, email, password, user_type)
values ('Jan', 'van der Waal', 'jan@carhop.com', 'vG5.jXf`', 'admin');
insert into users (first_name, last_name, email, password)
values ('Cris', 'Michelle', 'cmichelle2@w3.org', 'xM1)73''''YaJ&Jb');
insert into users (first_name, last_name, email, password)
values ('Fay', 'De Antoni', 'fdeantoni3@skype.com', 'eL4_H(Ex');
insert into users (first_name, last_name, email, password)
values ('Cad', 'Denington', 'cdenington4@ucoz.ru', 'mC0.Y?NG9>C}');
insert into users (first_name, last_name, email, password)
values ('Roxanna', 'Rumford', 'rrumford5@unicef.org', 'iD6=TmHu');
insert into users (first_name, last_name, email, password)
values ('Durante', 'Kingswood', 'dkingswood6@barnesandnoble.com', 'oS0(p`8r~5LflHm');
insert into users (first_name, last_name, email, password)
values ('Mayne', 'Babber', 'mbabber7@mapy.cz', 'mZ1/L&kx');
insert into users (first_name, last_name, email, password)
values ('Jamesy', 'Bremond', 'jbremond8@bloglovin.com', 'lP8@)"Tn}F');
insert into users (first_name, last_name, email, password)
values ('Agnola', 'Crunkhorn', 'acrunkhorn9@1688.com', 'hW5+z?!ywJ7');
insert into users (first_name, last_name, email, password)
values ('Cayla', 'Stubbe', 'cstubbea@ocn.ne.jp', 'mH8%S%P/k');
insert into users (first_name, last_name, email, password)
values ('Linnell', 'Knifton', 'lkniftonb@typepad.com', 'cW7#xnwH>');
insert into users (first_name, last_name, email, password)
values ('Amalle', 'O''Neill', 'aoneillc@1688.com', 'bY8''\47v>QB~?RD');
insert into users (first_name, last_name, email, password)
values ('Bjorn', 'Elies', 'beliesd@photobucket.com', 'tC5((6l$ACLG');
insert into users (first_name, last_name, email, password)
values ('Nicoli', 'Stanlake', 'nstanlakee@pagesperso-orange.fr', 'aP4"CRY7wqt');

insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (13, 'DC-69-LV', 76.38, true, 'Lexus', 'RX Hybrid', 2011, 6, 'd', 46075.54, 452.41, 396.44, 298, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (12, 'VJ-53-IV', 49.47, false, 'Mazda', 'Miata MX-5', 1993, 3, 'd', 35362.45, 443.05, 67.81, 2, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (11, 'NE-40-GY', 7.59, true, 'Acura', 'Legend', 1994, 1, 'l', 57950.82, 188.43, 307.28, 217, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (9, 'DQ-08-GD', 80.55, true, 'Dodge', 'Neon', 2002, 2, 'x', 38630.59, 251.98, 279.78, 50, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (1, 'OX-32-QK', 14.39, false, 'Chrysler', 'Aspen', 2008, 3, 'c', 44685.97, 246.29, 369.98, 476, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (9, 'VI-87-JG', 13.17, true, 'Chevrolet', 'Silverado 1500', 2011, 1, 'w', 29236.45, 81.98, 439.9, 383,
        'ELECTRIC', 'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (1, 'KE-79-NR', 36.91, false, 'Lotus', 'Exige', 2011, 5, 'z', 73942.83, 434.61, 437.25, 168, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (2, 'VR-28-GW', 24.53, false, 'BMW', '3 Series', 2011, 5, 'j', 31053.93, 425.95, 214.85, 271, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (3, 'NC-32-OI', 77.08, false, 'Hyundai', 'Sonata', 2010, 4, 'v', 63204.08, 332.03, 400.16, 227, 'ELECTRIC',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (7, 'MI-92-XJ', 79.34, true, 'Oldsmobile', '98', 1994, 5, 'u', 69295.71, 333.5, 206.9, 266, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (5, 'EI-97-JR', 97.96, true, 'Lamborghini', 'Diablo', 1993, 4, 'r', 30333.35, 147.55, 243.94, 366, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (3, 'XJ-07-UT', 10.82, true, 'Mazda', 'B2000', 1985, 3, 'y', 10621.73, 372.48, 359.42, 417, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (1, 'EF-75-RS', 6.0, true, 'Ferrari', 'California', 2010, 2, 'i', 50210.4, 140.83, 54.58, 451, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (5, 'BB-83-UY', 16.92, false, 'Rolls-Royce', 'Phantom', 2010, 5, 'p', 52485.08, 296.45, 323.01, 330, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (9, 'WH-94-XB', 25.69, false, 'Land Rover', 'Discovery', 2004, 2, 'z', 33397.48, 243.02, 368.29, 79, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (6, 'XJ-79-OM', 87.4, true, 'BMW', 'X6', 2011, 3, 'o', 54459.52, 476.63, 437.43, 157, 'HYBRID', 'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (10, 'AH-56-EN', 74.56, false, 'Mazda', 'Protege', 1994, 6, 'j', 17137.14, 354.88, 197.62, 348, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (3, 'NL-99-TI', 17.09, false, 'Toyota', 'Corolla', 1993, 2, 'a', 62155.26, 497.53, 161.56, 120, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (9, 'FA-56-TF', 16.97, false, 'Volvo', 'C70', 2011, 6, 'o', 17737.53, 235.64, 481.71, 15, 'HYBRID', 'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (9, 'CR-46-JV', 99.41, false, 'Lamborghini', 'Gallardo', 2012, 1, 'w', 17069.62, 172.58, 276.72, 81, 'HYBRID',
        'Automatic', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (15, 'ZH-77-IM', 64.7, false, 'Mitsubishi', 'Eclipse', 2010, 4, 'e', 47356.88, 371.6, 143.94, 442, 'GASOLINE',
        'Manual', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (6, 'SW-14-XZ', 84.84, false, 'Ford', 'Festiva', 1993, 3, 'c', 62717.9, 287.36, 118.1, 133, 'GASOLINE',
        'Manual', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (13, 'QC-08-MT', 13.2, true, 'Suzuki', 'Cultus', 1985, 4, 'o', 58242.18, 289.97, 191.83, 450, 'GASOLINE',
        'Manual', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (11, 'LM-45-GO', 53.41, false, 'GMC', 'Yukon', 1994, 3, 'l', 43922.04, 194.12, 424.46, 138, 'GASOLINE',
        'Manual', 51.583560, 4.796560);
insert into cars (owner_id, license_plate, rental_price, available, brand_name, model_name, build_year, num_of_seats,
                  emission_category, purchase_price, monthly_insurance_cost, yearly_maintenance_cost, range, fuel_type,
                  transmission, latitude, longitude)
values (7, 'MQ-64-TT', 78.58, false, 'Honda', 'Element', 2011, 2, 'h', 54080.76, 480.19, 427.62, 294, 'GASOLINE',
        'Automatic', 51.583560, 4.796560);


INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (1, 2, '2023-10-15', '2023-10-20', 250.00);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (3, 4, '2023-10-16', '2023-10-19', 180.50);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (5, 6, '2023-10-17', '2023-10-22', 320.75);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (7, 8, '2023-10-18', '2023-10-23', 200.25);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (9, 10, '2023-10-19', '2023-10-24', 280.00);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (11, 12, '2023-10-20', '2023-10-25', 225.75);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (13, 14, '2023-10-21', '2023-10-26', 175.50);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (15, 1, '2023-10-22', '2023-10-27', 275.25);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (2, 3, '2023-10-23', '2023-10-28', 300.00);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (4, 5, '2023-10-24', '2023-10-29', 225.75);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (6, 7, '2023-10-25', '2023-10-30', 190.50);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (8, 9, '2023-10-26', '2023-10-31', 210.25);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (10, 11, '2023-10-27', '2023-11-01', 280.00);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (12, 13, '2023-10-28', '2023-11-02', 250.75);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (14, 15, '2023-10-29', '2023-11-03', 160.50);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (1, 4, '2023-10-30', '2023-11-04', 235.25);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (3, 6, '2023-10-31', '2023-11-05', 310.00);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (5, 8, '2023-11-01', '2023-11-06', 215.75);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (7, 10, '2023-11-02', '2023-11-07', 280.50);
INSERT INTO Rentals (car_id, renter_id, start_date, end_date, cost)
VALUES (9, 12, '2023-11-03', '2023-11-08', 190.25);
